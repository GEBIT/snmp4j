/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOTableIndex.java  
  _## 
  _##  Copyright (C) 2005-2014  Frank Fock (SNMP4J.org)
  _##  
  _##  Licensed under the Apache License, Version 2.0 (the "License");
  _##  you may not use this file except in compliance with the License.
  _##  You may obtain a copy of the License at
  _##  
  _##      http://www.apache.org/licenses/LICENSE-2.0
  _##  
  _##  Unless required by applicable law or agreed to in writing, software
  _##  distributed under the License is distributed on an "AS IS" BASIS,
  _##  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  _##  See the License for the specific language governing permissions and
  _##  limitations under the License.
  _##  
  _##########################################################################*/


package org.snmp4j.agent.mo;

import org.snmp4j.smi.*;

/**
 * The <code>MOTableIndex</code> class represents a index definition of a
 * conceptual table. An index always has to implement also the
 * {@link MOTableIndexValidator} interface for validation of index values
 * for newly created rows.
 *
 * @author Frank Fock
 * @version 1.0
 */
public class MOTableIndex implements MOTableIndexValidator {

  public static final int MAX_INDEX_OID_LENGTH = 127;

  private MOTableSubIndex[] subindexes;
  private boolean impliedLength = false;
  private MOTableIndexValidator validator;


  /**
   * Creates a index definition from an array of sub-index definitions.
   * @param subIndexes
   *   an array of sub-index definitions with at least one element.
   */
  public MOTableIndex(MOTableSubIndex[] subIndexes) {
    if ((subIndexes == null) || (subIndexes.length < 1)) {
      throw new IllegalArgumentException(
          "Index definition must have at least one sub-index");
    }
    this.subindexes = subIndexes;
  }

  /**
   * Creates a index definition from an array of sub-index definitions where
   * the last sub-index may have an implied length.
   * @param subIndexes
   *   an array of sub-index definitions with at least one element.
   * @param impliedLength
   *   if <code>true</code> the last sub-index has an implied length if at has
   *   a variable length at all.
   */
  public MOTableIndex(MOTableSubIndex[] subIndexes, boolean impliedLength) {
    this(subIndexes);
    this.impliedLength = impliedLength;
  }

  /**
   * Creates a index definition from an array of sub-index definitions where
   * the last sub-index may have an implied length.
   * @param subIndexes
   *   an array of sub-index definitions with at least one element.
   * @param impliedLength
   *   if <code>true</code> the last sub-index has an implied length if at has
   *   a variable length at all.
   * @param validator
   *   an index validator that is called whenever a new index value needs to
   *   validated.
   */
  public MOTableIndex(MOTableSubIndex[] subIndexes, boolean impliedLength,
                      MOTableIndexValidator validator) {
    this(subIndexes, impliedLength);
    this.validator = validator;
  }

  /**
   * Gets the sub-index definition at the specified index.
   * @param index
   *    a valid sub-index index (zero-based).
   * @return
   *    the <code>MOTableSubIndex</code>.
   */
  public MOTableSubIndex getIndex(int index) {
    return this.subindexes[index];
  }

  public boolean isImpliedLength() {
    return impliedLength;
  }

  /**
   * Gets the index validator (if present).
   * @return
   *    the <code>MOTableIndexValidator</code> associated with this index or
   *    <code>null</code>.
   */
  public MOTableIndexValidator getValidator() {
    return validator;
  }

  /**
   * Sets the index validator associated with this index definition.
   * @param validator
   *    a <code>MOTableIndexValidator</code> instance.
   */
  public void setValidator(MOTableIndexValidator validator) {
    this.validator = validator;
  }

  /**
   * Gets the number of sub-index definitions in this index definition.
   * @return
   *    the sub-index count.
   */
  public int size() {
    return subindexes.length;
  }

  private static boolean checkIndexBytes(OID index, long start, long end) {
    if ((start < 0) || (start > MOTableIndex.MAX_INDEX_OID_LENGTH) ||
        (end < 0) || (end > MOTableIndex.MAX_INDEX_OID_LENGTH)) {
      return false;
    }
    for (int i=(int)start; ((i<index.size()) && (i<end)); i++) {
      if (index.getUnsigned(i) > 255) {
        return false;
      }
    }
    return true;
  }

  private static boolean isStringSyntax(int smiSyntax) {
    switch (smiSyntax) {
      case SMIConstants.SYNTAX_OCTET_STRING:
      case SMIConstants.SYNTAX_IPADDRESS:
      case SMIConstants.SYNTAX_OPAQUE: {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks whether an index OID is a valid index for this index definition
   * or not.
   * @param index
   *    an OID (possibly zero length).
   * @return
   *    <code>true</code> if the index is valid or <code>false</code> otherwise.
   */
  public boolean isValidIndex(OID index) {
    if (index.size() > MOTableIndex.MAX_INDEX_OID_LENGTH) {
      return false;
    }
    int l = 0;
    int i;
    for (i=0; ((i<size()) && (l < index.size())); i++) {
      MOTableSubIndex subIndex = getIndex(i);
      if ((i+1 == size()) && (isImpliedLength())) {
        int type = subIndex.getSmiSyntax();
        switch (type) {
          case SMIConstants.SYNTAX_OCTET_STRING:
          case SMIConstants.SYNTAX_IPADDRESS:
            if (!checkIndexBytes(index, l, index.size()))  {
              return false;
            }
            break;
        }
        return true;
      }
      else if ((subIndex.getMinLength() != subIndex.getMaxLength())) {
        if (index.size() < index.get(l)+1) {
          return false;
        }
        if ((index.get(l) < subIndex.getMinLength()) ||
            (index.get(l) > subIndex.getMaxLength())) {
          return false;
        }
        if (isStringSyntax(subIndex.getSmiSyntax())) {
          if (!checkIndexBytes(index, l, l+index.getUnsigned(l)+1)) {
            return false;
          }
        }
        l += index.getUnsigned(l)+1;
      }
      else {
        if (isStringSyntax(subIndex.getSmiSyntax())) {
          if (!checkIndexBytes(index, l, l+subIndex.getMaxLength())) {
            return false;
          }
        }
        // min == max
        l += subIndex.getMaxLength();
      }
    }
    return (((index.size() == l) && (i >= size())) &&
            ((validator == null) || (validator.isValidIndex(index))));
  }

  private static Variable getIndexVariable(MOTableSubIndex subIndexDef,
                                           OID subIndex,
                                           boolean impliedLength) {
    switch (subIndexDef.getSmiSyntax()) {
      case SMIConstants.SYNTAX_OCTET_STRING: {
        if ((impliedLength) ||
            (subIndexDef.getMinLength() == subIndexDef.getMaxLength())) {
          OctetString s = new OctetString(subIndex.toByteArray());
          return s;
        }
        OID suffix = new OID(subIndex.getValue(), 1, subIndex.size() - 1);
        return new OctetString(suffix.toByteArray());
      }
      case SMIConstants.SYNTAX_OBJECT_IDENTIFIER: {
        if ((impliedLength) ||
            (subIndexDef.getMinLength() == subIndexDef.getMaxLength())) {
          return subIndex;
        }
        OID suffix = new OID(subIndex.getValue(), 1, subIndex.size() - 1);
        return suffix;
      }
      case SMIConstants.SYNTAX_UNSIGNED_INTEGER32: {
        return new Gauge32(subIndex.get(subIndex.size()-1));
      }
      case SMIConstants.SYNTAX_TIMETICKS: {
        return new TimeTicks(subIndex.get(subIndex.size()-1));
      }
      case SMIConstants.SYNTAX_INTEGER: {
        return new Integer32(subIndex.get(subIndex.size()-1));
      }
      case SMIConstants.SYNTAX_IPADDRESS: {
        return new IpAddress(subIndex.toString());
      }
/*
      case SMIConstants.SYN_NETADDRESS: {
        String id = subIndex.toString();
        return new IpAddress(id.substring(id.indexOf(".") + 1));
      }
*/
    }
    return null;
  }

  /**
   * Split a table index into an array of object IDs each representing the
   * value of its corresponding index object. For example if a table's index
   * would be defined as INDEX { ifIndex, ipAddress } and the index given
   * would be "1.127.0.0.1" the resulting array would be { "1", "127.0.0.1" }
   *
   * @param index
   *    an OID denoting a table's index value.
   * @return
   *    an array of OID instances with the same size as returned by
   *    {@link #size}. If the given index is not a valid object ID
   *    <code>null</code> is returned.
   */
  public OID[] getIndexOIDs(OID index) {
    OID[] r = new OID[size()];
    int[] ind = index.getValue();
    int pos = 0;
    for (int i = 0; i < subindexes.length; i++) {
      if ((i+1 == size()) && (isImpliedLength())) {
        r[i] = new OID(ind, pos, index.size()-pos);
        break;
      }
      else if ((subindexes[i].getMinLength() != subindexes[i].getMaxLength())) {
        r[i] = new OID(ind, pos, index.get(pos) + 1);
      }
      else {
        r[i] = new OID(index.getValue(), pos, subindexes[i].getMaxLength());
      }
      pos += r[i].size();
    }
    return r;
  }

  /**
   * Gets the index values contained in an index OID.
   * @param index
   *   the index OID.
   * @return
   *    an array of values representing the index.
   * @see #getIndexOID
   */
  public Variable[] getIndexValues(OID index) {
    OID[] oids = getIndexOIDs(index);
    Variable[] values = new Variable[oids.length];
    for (int i=0; i<oids.length; i++) {
      boolean implied = ((isImpliedLength()) && (i+1 == size()));
      values[i] = getIndexVariable(subindexes[i], oids[i], implied);
    }
    return values;
  }

  /**
   * Gets the index OID from an array of index values.
   * @param indexValues
   *    an array of Variable instances that has to match the number and type
   *    of sub-indexes in this index.
   * @return
   *    the corresponding index OID.
   * @see #getIndexValues
   */
  public OID getIndexOID(Variable[] indexValues) {
    if (indexValues.length != size()) {
      throw new IllegalArgumentException("Index value length != size()");
    }
    OID index = new OID();
    for (int i=0; i<indexValues.length; i++) {
      if (indexValues[i].getSyntax() == this.subindexes[i].getSmiSyntax()) {
        index.append(indexValues[i].toSubIndex((i + 1 == indexValues.length) &&
                                               impliedLength));
      }
      else {
        throw new IllegalArgumentException("Syntax of index value #"+i+
                                           " = "+
                                           indexValues[i].getSyntaxString()+
                                           " does not match index definition "+
                                           AbstractVariable.getSyntaxString(
                                               this.subindexes[i].getSmiSyntax()));
      }
    }
    return index;
  }
}
