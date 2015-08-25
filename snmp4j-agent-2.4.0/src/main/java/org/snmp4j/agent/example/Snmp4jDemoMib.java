/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - Snmp4jDemoMib.java  
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

package org.snmp4j.agent.example;
//--AgentGen BEGIN=_BEGIN
//--AgentGen END

import org.snmp4j.smi.*;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.agent.*;
import org.snmp4j.agent.mo.*;
import org.snmp4j.agent.mo.snmp.*;
import org.snmp4j.agent.mo.snmp.smi.*;
import org.snmp4j.agent.request.*;
import org.snmp4j.log.LogFactory;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.agent.mo.snmp.tc.*;


//--AgentGen BEGIN=_IMPORT
//--AgentGen END

public class Snmp4jDemoMib
//--AgentGen BEGIN=_EXTENDS
//--AgentGen END
implements MOGroup
//--AgentGen BEGIN=_IMPLEMENTS
//--AgentGen END
{

  private static final LogAdapter LOGGER =
      LogFactory.getLogger(Snmp4jDemoMib.class);

//--AgentGen BEGIN=_STATIC
//--AgentGen END

  // Factory
  private MOFactory moFactory =
    DefaultMOFactory.getInstance();

  // Constants
  public static final OID oidSnmp4jDemoScalar =
    new OID(new int[] { 1,3,6,1,4,1,4976,10,1,1,20,1,1,0 });

  public static final OID oidSnmp4jDemoEvent =
    new OID(new int[] { 1,3,6,1,4,1,4976,10,1,1,20,2,0,1 });
  public static final OID oidTrapVarSnmp4jDemoEntryCol3 =
    new OID(new int[] { 1,3,6,1,4,1,4976,10,1,1,20,1,2,1,5 });
  public static final OID oidTrapVarSnmp4jDemoTableRowModification =
    new OID(new int[] { 1,3,6,1,4,1,4976,10,1,1,20,1,2,1,9 });


  // Enumerations

  public static final class Snmp4jDemoTableRowModificationEnum {
    public static final int created = 1;
    public static final int updated = 2;
    public static final int deleted = 3;
  }



  // TextualConventions
  private static final String TC_MODULE_SNMPV2_TC = "SNMPv2-TC";
  private static final String TC_DISPLAYSTRING = "DisplayString";
  private static final String TC_ROWSTATUS = "RowStatus";
  private static final String TC_STORAGETYPE = "StorageType";
  private static final String TC_TIMESTAMP = "TimeStamp";

  // Scalars
  private MOScalar snmp4jDemoScalar;

  // Tables
  public static final OID oidSnmp4jDemoEntry =
    new OID(new int[] { 1,3,6,1,4,1,4976,10,1,1,20,1,2,1 });

  // Index OID definitions
  public static final OID oidSnmp4jDemoEntryIndex1 =
    new OID(new int[] { 1,3,6,1,4,1,4976,10,1,1,20,1,2,1,1 });
  public static final OID oidSnmp4jDemoEntryIndex2 =
    new OID(new int[] { 1,3,6,1,4,1,4976,10,1,1,20,1,2,1,2 });

  // Column TC definitions for snmp4jDemoEntry:
  public static final String tcModuleSNMPv2Tc = "SNMPv2-TC";
  public static final String tcDefTimeStamp = "TimeStamp";
  public static final String tcDefStorageType = "StorageType";
  public static final String tcDefRowStatus = "RowStatus";

  // Column sub-identifer definitions for snmp4jDemoEntry:
  public static final int colSnmp4jDemoEntryCol1 = 3;
  public static final int colSnmp4jDemoEntryCol2 = 4;
  public static final int colSnmp4jDemoEntryCol3 = 5;
  public static final int colSnmp4jDemoEntryCol4 = 6;
  public static final int colSnmp4jDemoEntryCol5 = 7;
  public static final int colSnmp4jDemoEntryCol6 = 8;
  public static final int colSnmp4jDemoTableRowModification = 9;

  // Column index definitions for snmp4jDemoEntry:
  public static final int idxSnmp4jDemoEntryCol1 = 0;
  public static final int idxSnmp4jDemoEntryCol2 = 1;
  public static final int idxSnmp4jDemoEntryCol3 = 2;
  public static final int idxSnmp4jDemoEntryCol4 = 3;
  public static final int idxSnmp4jDemoEntryCol5 = 4;
  public static final int idxSnmp4jDemoEntryCol6 = 5;
  public static final int idxSnmp4jDemoTableRowModification = 6;

  private MOTableSubIndex[] snmp4jDemoEntryIndexes;
  private MOTableIndex snmp4jDemoEntryIndex;

  private MOTable<Snmp4jDemoEntryRow,MOColumn,MOTableModel<Snmp4jDemoEntryRow>>
      snmp4jDemoEntry;
  private MOTableModel<Snmp4jDemoEntryRow> snmp4jDemoEntryModel;


//--AgentGen BEGIN=_MEMBERS
//--AgentGen END

  /**
   * Constructs a Snmp4jDemoMib instance without actually creating its
   * <code>ManagedObject</code> instances. This has to be done in a
   * sub-class constructor or after construction by calling
   * {@link #createMO(MOFactory moFactory)}.
   */
  protected Snmp4jDemoMib() {
//--AgentGen BEGIN=_DEFAULTCONSTRUCTOR
//--AgentGen END
  }

  /**
   * Constructs a Snmp4jDemoMib instance and actually creates its
   * <code>ManagedObject</code> instances using the supplied
   * <code>MOFactory</code> (by calling
   * {@link #createMO(MOFactory moFactory)}).
   * @param moFactory
   *    the <code>MOFactory</code> to be used to create the
   *    managed objects for this module.
   */
  public Snmp4jDemoMib(MOFactory moFactory) {
    createMO(moFactory);
//--AgentGen BEGIN=_FACTORYCONSTRUCTOR
//--AgentGen END
  }

//--AgentGen BEGIN=_CONSTRUCTORS
//--AgentGen END

  /**
   * Create the ManagedObjects defined for this MIB module
   * using the specified {@link MOFactory}.
   * @param moFactory
   *    the <code>MOFactory</code> instance to use for object
   *    creation.
   */
  protected void createMO(MOFactory moFactory) {
    addTCsToFactory(moFactory);
    snmp4jDemoScalar =
      new Snmp4jDemoScalar(oidSnmp4jDemoScalar,
                           moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_WRITE));
    snmp4jDemoScalar.addMOValueValidationListener(new Snmp4jDemoScalarValidator());
    createSnmp4jDemoEntry(moFactory);
  }

  public MOScalar getSnmp4jDemoScalar() {
    return snmp4jDemoScalar;
  }


  public MOTable<Snmp4jDemoEntryRow,MOColumn,MOTableModel<Snmp4jDemoEntryRow>> getSnmp4jDemoEntry() {
    return snmp4jDemoEntry;
  }


  private void createSnmp4jDemoEntry(MOFactory moFactory) {
    // Index definition
    snmp4jDemoEntryIndexes =
      new MOTableSubIndex[] {
      moFactory.createSubIndex(oidSnmp4jDemoEntryIndex1,
                               SMIConstants.SYNTAX_INTEGER, 1, 1),
      moFactory.createSubIndex(oidSnmp4jDemoEntryIndex2,
                               SMIConstants.SYNTAX_OCTET_STRING, 1, 32)
    };

    snmp4jDemoEntryIndex =
      moFactory.createIndex(snmp4jDemoEntryIndexes, true);

    // Columns
    MOColumn[] snmp4jDemoEntryColumns = new MOColumn[7];
    snmp4jDemoEntryColumns[idxSnmp4jDemoEntryCol1] =
      new MOMutableColumn(colSnmp4jDemoEntryCol1,
                          SMIConstants.SYNTAX_INTEGER32,
                          moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_CREATE),
                          new Integer32(1));
    ((MOMutableColumn)snmp4jDemoEntryColumns[idxSnmp4jDemoEntryCol1]).
      addMOValueValidationListener(new Snmp4jDemoEntryCol1Validator());
    snmp4jDemoEntryColumns[idxSnmp4jDemoEntryCol2] =
      new MOMutableColumn(colSnmp4jDemoEntryCol2,
                          SMIConstants.SYNTAX_OCTET_STRING,
                          moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_CREATE),
                          new OctetString(new byte[] {  }));
    ValueConstraint snmp4jDemoEntryCol2VC = new ConstraintsImpl();
    ((ConstraintsImpl)snmp4jDemoEntryCol2VC).add(new Constraint(0L, 128L));
    ((MOMutableColumn)snmp4jDemoEntryColumns[idxSnmp4jDemoEntryCol2]).
      addMOValueValidationListener(new ValueConstraintValidator(snmp4jDemoEntryCol2VC));
    ((MOMutableColumn)snmp4jDemoEntryColumns[idxSnmp4jDemoEntryCol2]).
      addMOValueValidationListener(new Snmp4jDemoEntryCol2Validator());
    snmp4jDemoEntryColumns[idxSnmp4jDemoEntryCol3] =
      moFactory.createColumn(colSnmp4jDemoEntryCol3,
                             SMIConstants.SYNTAX_COUNTER32,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY));
    snmp4jDemoEntryColumns[idxSnmp4jDemoEntryCol4] =
      moFactory.createColumn(colSnmp4jDemoEntryCol4,
                             SMIConstants.SYNTAX_TIMETICKS,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY),
                             tcModuleSNMPv2Tc,
                             tcDefTimeStamp);
    snmp4jDemoEntryColumns[idxSnmp4jDemoEntryCol5] =
      new StorageType(colSnmp4jDemoEntryCol5,
                      moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_CREATE),
                      new Integer32(2));
    ValueConstraint snmp4jDemoEntryCol5VC = new EnumerationConstraint(
      new int[] { 1,
                  2,
                  3,
                  4,
                  5 });
    ((MOMutableColumn)snmp4jDemoEntryColumns[idxSnmp4jDemoEntryCol5]).
      addMOValueValidationListener(new ValueConstraintValidator(snmp4jDemoEntryCol5VC));
    ((MOMutableColumn)snmp4jDemoEntryColumns[idxSnmp4jDemoEntryCol5]).
      addMOValueValidationListener(new Snmp4jDemoEntryCol5Validator());
    snmp4jDemoEntryColumns[idxSnmp4jDemoEntryCol6] =
      new RowStatus(colSnmp4jDemoEntryCol6);
    ValueConstraint snmp4jDemoEntryCol6VC = new EnumerationConstraint(
      new int[] { 1,
                  2,
                  3,
                  4,
                  5,
                  6 });
    ((MOMutableColumn)snmp4jDemoEntryColumns[idxSnmp4jDemoEntryCol6]).
      addMOValueValidationListener(new ValueConstraintValidator(snmp4jDemoEntryCol6VC));
    ((MOMutableColumn)snmp4jDemoEntryColumns[idxSnmp4jDemoEntryCol6]).
      addMOValueValidationListener(new Snmp4jDemoEntryCol6Validator());
    snmp4jDemoEntryColumns[idxSnmp4jDemoTableRowModification] =
      moFactory.createColumn(colSnmp4jDemoTableRowModification,
                             SMIConstants.SYNTAX_INTEGER,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_NOTIFY));
    // Table model
    snmp4jDemoEntryModel =
      moFactory.createTableModel(oidSnmp4jDemoEntry,
                                 snmp4jDemoEntryIndex,
                                 snmp4jDemoEntryColumns);
    ((MOMutableTableModel<Snmp4jDemoEntryRow>)snmp4jDemoEntryModel).setRowFactory(
      new Snmp4jDemoEntryRowFactory());
    snmp4jDemoEntry =
      moFactory.createTable(oidSnmp4jDemoEntry,
                            snmp4jDemoEntryIndex,
                            snmp4jDemoEntryColumns,
                            snmp4jDemoEntryModel);
  }



  public void registerMOs(MOServer server, OctetString context)
    throws DuplicateRegistrationException
  {
    // Scalar Objects
    server.register(this.snmp4jDemoScalar, context);
    server.register(this.snmp4jDemoEntry, context);
//--AgentGen BEGIN=_registerMOs
//--AgentGen END
  }

  public void unregisterMOs(MOServer server, OctetString context) {
    // Scalar Objects
    server.unregister(this.snmp4jDemoScalar, context);
    server.unregister(this.snmp4jDemoEntry, context);
//--AgentGen BEGIN=_unregisterMOs
//--AgentGen END
  }

  // Notifications
  public void snmp4jDemoEvent(NotificationOriginator notificationOriginator,
                              OctetString context, VariableBinding[] vbs) {
    if (vbs.length < 2) {
      throw new IllegalArgumentException("Too few notification objects: "+
                                         vbs.length+"<2");
    }
    if (!(vbs[0].getOid().startsWith(oidTrapVarSnmp4jDemoEntryCol3))) {
      throw new IllegalArgumentException("Variable 0 has wrong OID: "+vbs[0].getOid()+
                                         " does not start with "+oidTrapVarSnmp4jDemoEntryCol3);
    }
    if (!snmp4jDemoEntryIndex.isValidIndex(snmp4jDemoEntry.getIndexPart(vbs[0].getOid()))) {
      throw new IllegalArgumentException("Illegal index for variable 0 specified: "+
                                         snmp4jDemoEntry.getIndexPart(vbs[0].getOid()));
    }
    if (!(vbs[1].getOid().startsWith(oidTrapVarSnmp4jDemoTableRowModification))) {
      throw new IllegalArgumentException("Variable 1 has wrong OID: "+vbs[1].getOid()+
                                         " does not start with "+oidTrapVarSnmp4jDemoTableRowModification);
    }
    if (!snmp4jDemoEntryIndex.isValidIndex(snmp4jDemoEntry.getIndexPart(vbs[1].getOid()))) {
      throw new IllegalArgumentException("Illegal index for variable 1 specified: "+
                                         snmp4jDemoEntry.getIndexPart(vbs[1].getOid()));
    }
    notificationOriginator.notify(context, oidSnmp4jDemoEvent, vbs);
  }


  // Scalars
  public class Snmp4jDemoScalar extends DisplayStringScalar<OctetString> {
    Snmp4jDemoScalar(OID oid, MOAccess access) {
      super(oid, access, new OctetString(),
            0,
            255);
//--AgentGen BEGIN=snmp4jDemoScalar
//--AgentGen END
    }

    public int isValueOK(SubRequest request) {
      Variable newValue =
        request.getVariableBinding().getVariable();
      int valueOK = super.isValueOK(request);
      if (valueOK != SnmpConstants.SNMP_ERROR_SUCCESS) {
      	return valueOK;
      }
      OctetString os = (OctetString)newValue;
      if (!(((os.length() >= 0) && (os.length() <= 255)))) {
        valueOK = SnmpConstants.SNMP_ERROR_WRONG_LENGTH;
      }
     //--AgentGen BEGIN=snmp4jDemoScalar::isValueOK
     //--AgentGen END
      return valueOK;
    }

    public OctetString getValue() {
     //--AgentGen BEGIN=snmp4jDemoScalar::getValue
     //--AgentGen END
      return super.getValue();
    }

    public int setValue(OctetString newValue) {
     //--AgentGen BEGIN=snmp4jDemoScalar::setValue
     //--AgentGen END
      return super.setValue(newValue);
    }

     //--AgentGen BEGIN=snmp4jDemoScalar::_METHODS
     //--AgentGen END

  }


  // Value Validators
  /**
   * The <code>Snmp4jDemoScalarValidator</code> implements the value
   * validation for <code>Snmp4jDemoScalar</code>.
   */
  static class Snmp4jDemoScalarValidator implements MOValueValidationListener {

    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      OctetString os = (OctetString)newValue;
      if (!(((os.length() >= 0) && (os.length() <= 255)))) {
        validationEvent.setValidationStatus(SnmpConstants.SNMP_ERROR_WRONG_LENGTH);
        return;
      }
     //--AgentGen BEGIN=snmp4jDemoScalar::validate
     //--AgentGen END
    }
  }

  /**
   * The <code>Snmp4jDemoEntryCol1Validator</code> implements the value
   * validation for <code>Snmp4jDemoEntryCol1</code>.
   */
  static class Snmp4jDemoEntryCol1Validator implements MOValueValidationListener {

    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
     //--AgentGen BEGIN=snmp4jDemoEntryCol1::validate
     //--AgentGen END
    }
  }
  /**
   * The <code>Snmp4jDemoEntryCol2Validator</code> implements the value
   * validation for <code>Snmp4jDemoEntryCol2</code>.
   */
  static class Snmp4jDemoEntryCol2Validator implements MOValueValidationListener {

    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      OctetString os = (OctetString)newValue;
      if (!(((os.length() >= 0) && (os.length() <= 128)))) {
        validationEvent.setValidationStatus(SnmpConstants.SNMP_ERROR_WRONG_LENGTH);
        return;
      }
     //--AgentGen BEGIN=snmp4jDemoEntryCol2::validate
     //--AgentGen END
    }
  }
  /**
   * The <code>Snmp4jDemoEntryCol5Validator</code> implements the value
   * validation for <code>Snmp4jDemoEntryCol5</code>.
   */
  static class Snmp4jDemoEntryCol5Validator implements MOValueValidationListener {

    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
     //--AgentGen BEGIN=snmp4jDemoEntryCol5::validate
     //--AgentGen END
    }
  }
  /**
   * The <code>Snmp4jDemoEntryCol6Validator</code> implements the value
   * validation for <code>Snmp4jDemoEntryCol6</code>.
   */
  static class Snmp4jDemoEntryCol6Validator implements MOValueValidationListener {

    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
     //--AgentGen BEGIN=snmp4jDemoEntryCol6::validate
     //--AgentGen END
    }
  }

  // Rows and Factories

  public class Snmp4jDemoEntryRow extends DefaultMOMutableRow2PC {

     //--AgentGen BEGIN=snmp4jDemoEntry::RowMembers
     //--AgentGen END

    public Snmp4jDemoEntryRow(OID index, Variable[] values) {
      super(index, values);
     //--AgentGen BEGIN=snmp4jDemoEntry::RowConstructor
     //--AgentGen END
    }

    public Integer32 getSnmp4jDemoEntryCol1() {
     //--AgentGen BEGIN=snmp4jDemoEntry::getSnmp4jDemoEntryCol1
     //--AgentGen END
      return (Integer32) super.getValue(idxSnmp4jDemoEntryCol1);
    }

    public void setSnmp4jDemoEntryCol1(Integer32 newValue) {
     //--AgentGen BEGIN=snmp4jDemoEntry::setSnmp4jDemoEntryCol1
     //--AgentGen END
      super.setValue(idxSnmp4jDemoEntryCol1, newValue);
    }

    public OctetString getSnmp4jDemoEntryCol2() {
     //--AgentGen BEGIN=snmp4jDemoEntry::getSnmp4jDemoEntryCol2
     //--AgentGen END
      return (OctetString) super.getValue(idxSnmp4jDemoEntryCol2);
    }

    public void setSnmp4jDemoEntryCol2(OctetString newValue) {
     //--AgentGen BEGIN=snmp4jDemoEntry::setSnmp4jDemoEntryCol2
     //--AgentGen END
      super.setValue(idxSnmp4jDemoEntryCol2, newValue);
    }

    public Counter32 getSnmp4jDemoEntryCol3() {
     //--AgentGen BEGIN=snmp4jDemoEntry::getSnmp4jDemoEntryCol3
     //--AgentGen END
      return (Counter32) super.getValue(idxSnmp4jDemoEntryCol3);
    }

    public void setSnmp4jDemoEntryCol3(Counter32 newValue) {
     //--AgentGen BEGIN=snmp4jDemoEntry::setSnmp4jDemoEntryCol3
     //--AgentGen END
      super.setValue(idxSnmp4jDemoEntryCol3, newValue);
    }

    public TimeTicks getSnmp4jDemoEntryCol4() {
     //--AgentGen BEGIN=snmp4jDemoEntry::getSnmp4jDemoEntryCol4
     //--AgentGen END
      return (TimeTicks) super.getValue(idxSnmp4jDemoEntryCol4);
    }

    public void setSnmp4jDemoEntryCol4(TimeTicks newValue) {
     //--AgentGen BEGIN=snmp4jDemoEntry::setSnmp4jDemoEntryCol4
     //--AgentGen END
      super.setValue(idxSnmp4jDemoEntryCol4, newValue);
    }

    public Integer32 getSnmp4jDemoEntryCol5() {
     //--AgentGen BEGIN=snmp4jDemoEntry::getSnmp4jDemoEntryCol5
     //--AgentGen END
      return (Integer32) super.getValue(idxSnmp4jDemoEntryCol5);
    }

    public void setSnmp4jDemoEntryCol5(Integer32 newValue) {
     //--AgentGen BEGIN=snmp4jDemoEntry::setSnmp4jDemoEntryCol5
     //--AgentGen END
      super.setValue(idxSnmp4jDemoEntryCol5, newValue);
    }

    public Integer32 getSnmp4jDemoEntryCol6() {
     //--AgentGen BEGIN=snmp4jDemoEntry::getSnmp4jDemoEntryCol6
     //--AgentGen END
      return (Integer32) super.getValue(idxSnmp4jDemoEntryCol6);
    }

    public void setSnmp4jDemoEntryCol6(Integer32 newValue) {
     //--AgentGen BEGIN=snmp4jDemoEntry::setSnmp4jDemoEntryCol6
     //--AgentGen END
      super.setValue(idxSnmp4jDemoEntryCol6, newValue);
    }

    public Integer32 getSnmp4jDemoTableRowModification() {
     //--AgentGen BEGIN=snmp4jDemoEntry::getSnmp4jDemoTableRowModification
     //--AgentGen END
      return (Integer32) super.getValue(idxSnmp4jDemoTableRowModification);
    }

    public void setSnmp4jDemoTableRowModification(Integer32 newValue) {
     //--AgentGen BEGIN=snmp4jDemoEntry::setSnmp4jDemoTableRowModification
     //--AgentGen END
      super.setValue(idxSnmp4jDemoTableRowModification, newValue);
    }

    public Variable getValue(int column) {
     //--AgentGen BEGIN=snmp4jDemoEntry::RowGetValue
     //--AgentGen END
      switch(column) {
        case idxSnmp4jDemoEntryCol1:
        	return getSnmp4jDemoEntryCol1();
        case idxSnmp4jDemoEntryCol2:
        	return getSnmp4jDemoEntryCol2();
        case idxSnmp4jDemoEntryCol3:
        	return getSnmp4jDemoEntryCol3();
        case idxSnmp4jDemoEntryCol4:
        	return getSnmp4jDemoEntryCol4();
        case idxSnmp4jDemoEntryCol5:
        	return getSnmp4jDemoEntryCol5();
        case idxSnmp4jDemoEntryCol6:
        	return getSnmp4jDemoEntryCol6();
        case idxSnmp4jDemoTableRowModification:
        	return getSnmp4jDemoTableRowModification();
        default:
          return super.getValue(column);
      }
    }

    public void setValue(int column, Variable value) {
     //--AgentGen BEGIN=snmp4jDemoEntry::RowSetValue
     //--AgentGen END
      switch(column) {
        case idxSnmp4jDemoEntryCol1:
        	setSnmp4jDemoEntryCol1((Integer32)value);
        	break;
        case idxSnmp4jDemoEntryCol2:
        	setSnmp4jDemoEntryCol2((OctetString)value);
        	break;
        case idxSnmp4jDemoEntryCol3:
        	setSnmp4jDemoEntryCol3((Counter32)value);
        	break;
        case idxSnmp4jDemoEntryCol4:
        	setSnmp4jDemoEntryCol4((TimeTicks)value);
        	break;
        case idxSnmp4jDemoEntryCol5:
        	setSnmp4jDemoEntryCol5((Integer32)value);
        	break;
        case idxSnmp4jDemoEntryCol6:
        	setSnmp4jDemoEntryCol6((Integer32)value);
        	break;
        case idxSnmp4jDemoTableRowModification:
        	setSnmp4jDemoTableRowModification((Integer32)value);
        	break;
        default:
          super.setValue(column, value);
      }
    }

     //--AgentGen BEGIN=snmp4jDemoEntry::Row
     //--AgentGen END
  }

  class Snmp4jDemoEntryRowFactory
        implements MOTableRowFactory<Snmp4jDemoEntryRow>
  {
    public synchronized Snmp4jDemoEntryRow createRow(OID index, Variable[] values)
        throws UnsupportedOperationException
    {
      Snmp4jDemoEntryRow row =
        new Snmp4jDemoEntryRow(index, values);
     //--AgentGen BEGIN=snmp4jDemoEntry::createRow
     //--AgentGen END
      return row;
    }

    public synchronized void freeRow(Snmp4jDemoEntryRow row) {
     //--AgentGen BEGIN=snmp4jDemoEntry::freeRow
     //--AgentGen END
    }

     //--AgentGen BEGIN=snmp4jDemoEntry::RowFactory
     //--AgentGen END
  }


//--AgentGen BEGIN=_METHODS
//--AgentGen END

  // Textual Definitions of MIB module Snmp4jDemoMib
  protected void addTCsToFactory(MOFactory moFactory) {
  }


//--AgentGen BEGIN=_TC_CLASSES_IMPORTED_MODULES_BEGIN
//--AgentGen END

  // Textual Definitions of other MIB modules
  protected void addImportedTCsToFactory(MOFactory moFactory) {
  }


//--AgentGen BEGIN=_TC_CLASSES_IMPORTED_MODULES_END
//--AgentGen END

//--AgentGen BEGIN=_CLASSES
//--AgentGen END

//--AgentGen BEGIN=_END
//--AgentGen END
}


