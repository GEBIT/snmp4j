/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - DefaultMOFactory.java  
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

import org.snmp4j.agent.*;
import org.snmp4j.agent.mo.snmp.SNMPv2MIB;
import org.snmp4j.agent.mo.snmp.SysUpTime;
import org.snmp4j.smi.*;
import java.util.Map;
import org.snmp4j.agent.mo.snmp.tc.TextualConvention;
import java.util.HashMap;
import org.snmp4j.agent.mo.snmp.SNMPv2TC;
import java.util.*;

/**
 * The <code>DefaultMOFactory</code> is the default factory for creating
 * ManagedObjects. The default factory creates columnar and scalar objects
 * based on SNMPv2-TC textual conventions with appropriate constraints.
 * Other textual conventions can be added too.
 *
 * @author Frank Fock
 * @version 2.3.0
 */
public class DefaultMOFactory implements MOFactory, LinkedMOFactory {

  private Map<String, Map<String, TextualConvention>> textualConventions =
      new HashMap<String, Map<String, TextualConvention>>();
  private Map<OID, Object> links;

  private static MOFactory instance;

  protected DefaultMOFactory() {
  }

  /**
   * Returns the factory singleton with default support for SNMPv2-TC textual
   * conventions.
   *
   * @return
   *    a MOFactory instance.
   */
  public static MOFactory getInstance() {
    if (instance == null) {
      instance = new DefaultMOFactory();
      addSNMPv2TCs(instance);
    }
    return instance;
  }

  /**
   * Sets the singleton factory.
   * @param factory
   *    a MOFactory instance.
   */
  public static void setInstance(MOFactory factory) {
    instance = factory;
  }

  /**
   * Adds support for SNMPv2TC textual conventions to the supplied ManagedObject
   * factory.
   * @param factory
   *    a MOFactory instance.
   */
  public static void addSNMPv2TCs(MOFactory factory) {
    Collection<TextualConvention> tcs = new SNMPv2TC().getTextualConventions();
    for (TextualConvention tc : tcs) {
      factory.addTextualConvention(tc);
    }
  }

  protected Map<? extends String, ? extends Map<String, TextualConvention>> getTextualConventions() {
    return textualConventions;
  }

  /**
   * Adds a textual convention to this factory which can then be used by the
   * factory to create appropriate value constraints for columnar and scalar
   * managed objects.
   * @param tc
   *    a TextualConvention instance.
   */
  public synchronized void addTextualConvention(TextualConvention tc) {
    Map<String, TextualConvention> tcMap = textualConventions.get(tc.getModuleName());
    if (tcMap == null) {
      tcMap = new HashMap<String, TextualConvention>(10);
      textualConventions.put(tc.getModuleName(), tcMap);
    }
    tcMap.put(tc.getName(), tc);
  }

  public synchronized void removeTextualConvention(TextualConvention tc) {
    Map<String, TextualConvention> tcMap = textualConventions.get(tc.getModuleName());
    if (tcMap != null) {
      tcMap.remove(tc.getName());
      if (tcMap.isEmpty()) {
        textualConventions.remove(tc.getModuleName());
      }
    }
  }

  @SuppressWarnings("unchecked")
  public synchronized <V extends Variable> TextualConvention<V>
      getTextualConvention(String moduleName, String name) {
    Map<String, TextualConvention> tcMap = textualConventions.get(moduleName);
    if (tcMap != null) {
      return tcMap.get(name);
    }
    return null;
  }

  public <V extends Variable> MOColumn<V> createColumn(int columnID, int syntax, MOAccess access) {
    return new MOMutableColumn<V>(columnID, syntax, access);
  }

  public <V extends Variable> MOColumn<V> createColumn(int columnID, int syntax, MOAccess access,
                                                       V defaultValue, boolean mutableInService) {
    return new MOMutableColumn<V>(columnID, syntax, access, defaultValue, mutableInService);
  }

  public <BaseRow extends MOTableRow, DependentRow extends MOTableRow>
    MOTableRelation<BaseRow,DependentRow> createTableRelation(
      MOTable<BaseRow, ? extends MOColumn, ? extends MOTableModel<BaseRow>> baseTable,
      MOTable<DependentRow, ? extends MOColumn, ? extends MOTableModel<DependentRow>> dependentTable) {
    return new MOTableRelation<BaseRow,DependentRow>(baseTable, dependentTable);
  }

  @Override
  public SysUpTime getSysUpTime(OctetString context) {
    return SNMPv2MIB.getSysUpTime(context);
  }

  public MOTableRow createRow(OID index, Variable[] values) throws
      UnsupportedOperationException {
    return new DefaultMOMutableRow2PC(index, values);
  }

  public <V extends Variable> MOScalar<V> createScalar(OID id, MOAccess access, V value) {
    return new MOScalar<V>(id, access, value);
  }

  @SuppressWarnings("unchecked")
  public <R extends MOTableRow, M extends MOTableModel<R>> MOTable<R, MOColumn, M>
      createTable(OID oid, MOTableIndex indexDef, MOColumn[] columns) {
    return createTable(oid, indexDef, columns, (M) createTableModel(oid, indexDef, columns));
  }

  public <R extends MOTableRow,  M extends MOTableModel<R>> MOTable<R, MOColumn, M>
      createTable(OID oid, MOTableIndex indexDef, MOColumn[] columns, M model) {
    return new DefaultMOTable<R, MOColumn, M>(oid, indexDef, columns, model);
  }

  @SuppressWarnings("unchecked")
  public <R extends MOTableRow, M extends MOTableModel<? extends R>> M createTableModel(OID tableOID,
                                       MOTableIndex indexDef,
                                       MOColumn[] columns) {
    return (M) new DefaultMOMutableTableModel<R>();
  }

  public void freeRow(MOTableRow row) {
  }

  public MOTableIndex createIndex(MOTableSubIndex[] subIndexes,
                                  boolean impliedLength) {
    return new MOTableIndex(subIndexes, impliedLength);
  }

  public MOTableSubIndex createSubIndex(int smiSyntax) {
    return new MOTableSubIndex(smiSyntax);
  }

  public MOTableSubIndex createSubIndex(OID oid, int smiSyntax) {
    return new MOTableSubIndex(oid, smiSyntax);
  }

  public MOTableSubIndex createSubIndex(int smiSyntax, int minLength,
                                        int maxLength) {
    return new MOTableSubIndex(smiSyntax, minLength, maxLength);
  }

  public MOTableSubIndex createSubIndex(OID oid, int smiSyntax, int minLength,
                                        int maxLength) {
    return new MOTableSubIndex(oid, smiSyntax, minLength, maxLength);
  }

  public MOTableIndex createIndex(MOTableSubIndex[] subIndexes,
                                  boolean impliedLength,
                                  MOTableIndexValidator validator) {
    return new MOTableIndex(subIndexes, impliedLength, validator);
  }

  public <V extends Variable> MOColumn<V> createColumn(int columnID, int syntax, MOAccess access,
                               String tcModuleName, String textualConvention) {
    TextualConvention<V> tc = getTextualConvention(tcModuleName, textualConvention);
    if (tc != null) {
      return tc.createColumn(columnID, syntax, access, (V)null, true);
    }
    return createColumn(columnID, syntax, access);
  }

  public <V extends Variable> MOColumn<V> createColumn(int columnID, int syntax, MOAccess access,
                               V defaultValue, boolean mutableInService,
                               String tcModuleName, String textualConvention) {
    TextualConvention<V> tc =
        getTextualConvention(tcModuleName, textualConvention);
    if (tc != null) {
      return tc.createColumn(columnID, syntax, access,
                             defaultValue, mutableInService);
    }
    return createColumn(columnID, syntax, access,
                        defaultValue, mutableInService);
  }

  public <V extends Variable> MOScalar<V> createScalar(OID id, MOAccess access, V value,
                               String tcModuleName, String textualConvention) {
    TextualConvention<V> tc =
        getTextualConvention(tcModuleName, textualConvention);
    if (tc != null) {
      return tc.createScalar(id, access, value);
    }
    return createScalar(id, access, value);
  }

  public MOAccess createAccess(int moAccess) {
    return MOAccessImpl.getInstance(moAccess);
  }

  public void setLink(OID oid, Object instrumentationHelperObject) {
    if (links == null) {
      initLinkMap();
    }
    links.put(oid, instrumentationHelperObject);
  }

  public Object getLink(OID oid) {
    if (links != null) {
      OID searchOID = new OID(oid);
      Object result = null;
      while ((searchOID.size() > 0) &&
             ((result = links.get(searchOID)) == null)) {
        searchOID.trim(1);
      }
      return result;
    }
    return null;
  }

  protected synchronized void initLinkMap() {
    if (links == null) {
      links = new HashMap<OID, Object>();
    }
  }
}
