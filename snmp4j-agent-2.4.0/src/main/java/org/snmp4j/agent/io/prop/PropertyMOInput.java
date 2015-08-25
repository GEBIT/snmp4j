/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - PropertyMOInput.java  
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

package org.snmp4j.agent.io.prop;

import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.Map.Entry;

import org.snmp4j.SNMP4JSettings;
import org.snmp4j.agent.io.*;
import org.snmp4j.smi.*;
import org.snmp4j.agent.mo.util.VariableProvider;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.LogFactory;
import org.snmp4j.util.OIDTextFormat;
import org.snmp4j.util.SimpleOIDTextFormat;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * The <code>PropertyMOInput</code> can be used to load MIB data from a
 * properties file.
 * <p>
 * The format of the properties file is as follows:
 * <pre>
 *   snmp4j.agent.cfg.contexts={s|x}&lt;context1&gt;[,{s|x}&lt;context2&gt;...]
 *   snmp4j.agent.cfg.oid[.ctx.&lt;context&gt;].&lt;oid&gt;=[&lt;numRows&gt;:&lt;numCols&gt;]
 *   snmp4j.agent.cfg.oid[.ctx.&lt;context&gt;].&lt;oid&gt;=[{&lt;format&gt;}&lt;value&gt;]
 *   snmp4j.agent.cfg.index[.ctx.&lt;context&gt;].oid.&lt;rowIndex&gt;={o}&lt;index&gt;
 *   snmp4j.agent.cfg.value[.ctx.&lt;context&gt;].oid.&lt;rowIndex&gt;.&lt;colIndex>=[{&lt;format&gt;}&lt;value&gt;]
 * </pre>
 * where text enclosed in [] is optional and
 * <ul>
 * <li>context1 - is a SNMPv3 context name as UTF-8 string (format {s}) or
 * a hexadecimal string (format {x}).
 * </li>
 * <li>context - is a SNMPv3 context name as UTF-8 string if that string does
 * not contain any ISO control characters, otherwise a hexdecimal representation
 * separated by a colon (:).
 * </li>
 * <li>format - one of
 * <ul>
 * <li>u - an Unsigned32 value.</li>
 * <li>i - an Integer32 value.</li>
 * <li>s - an OctetString value.</li>
 * <li>x - an OctetString value in hexadecimal format (separated by :).</li>
 * <li>d - an OctetString value in decimal format (separated by .).</li>
 * <li>b - an OctetString value in decimal format (separated by ' ' per byte).</li>
 * <li>n - a Null value.</li>
 * <li>o - an OID value as dotted string where string parts may be specified
 * directly enclosed in single quotes (') and the to an OID converted value
 * of a variable/oid instance may be specified in the format
 * <code>[#]{&lt;name/oid&gt;}</code>. The value of the variable will be included into
 * the OID with prependend length if the # is used in the format string
 * otherwise no length will be included.</li>
 * <li>t - a TimeTicks value as an unsigned long value.</li>
 * <li>a - a IpAddress value.</li>
 * <li>$ - gets the value from the variable or object instance specified by the
 * name/oid following the $.</li>
 * </ul>
 * </li>
 * <li>value - a variable value in the format specified by <code>format</code>.
 * </li>
 * <li>numRows - the number of rows in the table.</li>
 * <li>numCols - the number of columns in the table.</li>
 * <li>rowIndex - the row index as a zero based unsigned integer.</li>
 * <li>colIndex - the column index as a zero based unsigned integer.</li>
 * <li>index - the OID value of the row's index.</li>
 * </ul>
 * <p>
 * An example properties file is:
 * <pre>
 * snmp4j.agent.cfg.contexts=
 * snmp4j.agent.cfg.oid.1.3.6.1.2.1.1.2.0={o}1.3.6.1.4.1.4976
 * snmp4j.agent.cfg.oid.1.3.6.1.2.1.1.4.0={s}System Administrator
 * snmp4j.agent.cfg.oid.1.3.6.1.2.1.1.6.0={s}<edit location>
 * snmp4j.agent.cfg.oid.1.3.6.1.2.1.1.7.0={i}10
 * snmp4j.agent.cfg.oid.1.3.6.1.2.1.1.9.1=1:2
 * snmp4j.agent.cfg.index.1.3.6.1.2.1.1.9.1.0={o}1
 * snmp4j.agent.cfg.value.1.3.6.1.2.1.1.9.1.0.0={o}1.3.6.1.4.1.4976.10.1.1.100.4.1
 * snmp4j.agent.cfg.value.1.3.6.1.2.1.1.9.1.0.1=
 * ## VACM MIB
 * # security2Group
 * snmp4j.agent.cfg.oid.1.3.6.1.6.3.16.1.2.1=2:3
 * snmp4j.agent.cfg.index.1.3.6.1.6.3.16.1.2.1.0={o}2.6.'public'
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.16.1.2.1.0.0={s}v1v2cgroup
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.16.1.2.1.0.1={i}4
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.16.1.2.1.0.2={i}1
 * snmp4j.agent.cfg.index.1.3.6.1.6.3.16.1.2.1.1={o}3.6.'SHADES'
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.16.1.2.1.1.0={s}v3group
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.16.1.2.1.1.1={i}4
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.16.1.2.1.1.2={i}1
 * # access
 * snmp4j.agent.cfg.oid.1.3.6.1.6.3.16.1.4.1=2:6
 * snmp4j.agent.cfg.index.1.3.6.1.6.3.16.1.4.1.0={o}10.'v1v2cgroup'.0.2.1
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.16.1.4.1.0.0={i}1
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.16.1.4.1.0.1={s}unrestrictedReadView
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.16.1.4.1.0.2={s}unrestrictedWriteView
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.16.1.4.1.0.3={s}unrestrictedNotifyView
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.16.1.4.1.0.4={i}4
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.16.1.4.1.0.5={i}1
 * snmp4j.agent.cfg.index.1.3.6.1.6.3.16.1.4.1.1={o}7.'v3group'.0.3.3
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.16.1.4.1.1.0={i}1
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.16.1.4.1.1.1={s}unrestrictedReadView
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.16.1.4.1.1.2={s}unrestrictedWriteView
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.16.1.4.1.1.3={s}unrestrictedNotifyView
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.16.1.4.1.1.4={i}4
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.16.1.4.1.1.5={i}1
 * # view trees
 * snmp4j.agent.cfg.oid.1.3.6.1.6.3.16.1.5.2.1=3:4
 * snmp4j.agent.cfg.index.1.3.6.1.6.3.16.1.5.2.1.0={o}20.'unrestrictedReadView'.3.1.3.6
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.16.1.5.2.1.0.0={s}
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.16.1.5.2.1.0.1={i}1
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.16.1.5.2.1.0.2={i}4
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.16.1.5.2.1.0.3={i}1
 * snmp4j.agent.cfg.index.1.3.6.1.6.3.16.1.5.2.1.1={o}21.'unrestrictedWriteView'.3.1.3.6
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.16.1.5.2.1.1.0={s}
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.16.1.5.2.1.1.1={i}1
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.16.1.5.2.1.1.2={i}4
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.16.1.5.2.1.1.3={i}1
 * snmp4j.agent.cfg.index.1.3.6.1.6.3.16.1.5.2.1.2={o}22.'unrestrictedNotifyView'.3.1.3.6
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.16.1.5.2.1.2.0={s}
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.16.1.5.2.1.2.1={i}1
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.16.1.5.2.1.2.2={i}4
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.16.1.5.2.1.2.3={i}1
 * ## SNMP community MIB
 * snmp4j.agent.cfg.oid.1.3.6.1.6.3.18.1.1.1=1:7
 * snmp4j.agent.cfg.index.1.3.6.1.6.3.18.1.1.1.0={o}'public'
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.18.1.1.1.0.0={s}public
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.18.1.1.1.0.1={s}public
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.18.1.1.1.0.2={$1.3.6.1.6.3.10.2.1.1.0}
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.18.1.1.1.0.3={s}
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.18.1.1.1.0.4={s}
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.18.1.1.1.0.5={i}4
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.18.1.1.1.0.6={i}1
 * ## USM MIB
 * snmp4j.agent.cfg.oid.1.3.6.1.6.3.15.1.2.2.1=1:14
 * snmp4j.agent.cfg.index.1.3.6.1.6.3.15.1.2.2.1.0={o}$#{1.3.6.1.6.3.10.2.1.1.0}.6.'SHADES'
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.15.1.2.2.1.0.0={s}SHADES
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.15.1.2.2.1.0.1={o}
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.15.1.2.2.1.0.2={o}1.3.6.1.6.3.10.1.1.3
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.15.1.2.2.1.0.3={s}
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.15.1.2.2.1.0.4={s}
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.15.1.2.2.1.0.5={o}1.3.6.1.6.3.10.1.2.2
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.15.1.2.2.1.0.6={s}
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.15.1.2.2.1.0.7={s}
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.15.1.2.2.1.0.8={s}
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.15.1.2.2.1.0.9={i}4
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.15.1.2.2.1.0.10={i}1
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.15.1.2.2.1.0.11={s}SHADESAuthPassword
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.15.1.2.2.1.0.12={s}SHADESPrivPassword
 * snmp4j.agent.cfg.value.1.3.6.1.6.3.15.1.2.2.1.0.13=
 *
 * </pre>
 *
 * @author Frank Fock
 * @version 1.2
 */
public class PropertyMOInput implements MOInput {

  private static final LogAdapter logger =
      LogFactory.getLogger(PropertyMOInput.class);

  public static final String CONFIG_PREFIX = "snmp4j.agent.cfg.";
  public static final String CONTEXTS_ID = "contexts";
  public static final String OID_ID = "oid.";
  public static final String INDEX_ID = "index.";
  public static final String VERSION_ID = "version.";
  public static final String CTX_ID = "ctx.";
  public static final String VALUE_ID = "value.";

  private static final int STATE_ALL_CTX_DATA_SEQ = 0;
  private static final int STATE_ALL_CTX_DATA = 1;
  private static final int STATE_CTX_SEQ = 2;
  private static final int STATE_CTX_DATA_SEQ = 3;
  private static final int STATE_CTX_DATA = 4;

  private int importMode = ImportModes.REPLACE_CREATE;

  private int state = STATE_ALL_CTX_DATA_SEQ;
  private SortedMap<String,String> properties;
  private ContextInfo contexts;
  private OIDInfo oids;
  private DataInfo<?> data;
  private VariableProvider variables;
  private OIDTextFormat oidTextFormat = new SimpleOIDTextFormat();

  public PropertyMOInput(Properties props) {
    properties = scanProperties(props);
    contexts = scanContexts(properties);
  }

  public PropertyMOInput(Properties props, VariableProvider variables) {
    this(props);
    this.variables = variables;
  }

  public PropertyMOInput(Properties props, VariableProvider variables, OIDTextFormat oidTextFormat) {
    this(props, variables);
    this.oidTextFormat = oidTextFormat;
  }

  private ContextInfo scanContexts(Map properties) {
    String ctx = (String) properties.get(CONFIG_PREFIX+CONTEXTS_ID);
    StringTokenizer st = new StringTokenizer(ctx, ",");
    ArrayList<Context> l = new ArrayList<Context>(st.countTokens());
    while (st.hasMoreTokens()) {
      String token = st.nextToken();
      OctetString s = (OctetString)
          createVariableFromString(token, OctetString.class);
      l.add(new Context(s));
    }
    return new ContextInfo(l.size(), l.iterator());
  }

  private OIDInfo scanOIDs(SortedMap<String,String> properties, OctetString ctx) {
    String prefix = CONFIG_PREFIX+OID_ID;
    String ctxSuffix = "";
    if (ctx != null) {
      ctxSuffix = CTX_ID+ctx.toString()+".";
    }
    prefix += ctxSuffix;
    SortedMap<String,String> oids = properties.tailMap(prefix);
    ArrayList<MOInfo> l = new ArrayList<MOInfo>();
    for (Entry<String, String> stringStringEntry : oids.entrySet()) {
      Entry e = (Entry) stringStringEntry;
      String k = (String) e.getKey();
      if (k.startsWith(prefix)) {
        String oid;
        oid = k.substring(prefix.length());
        String version = properties.get(CONFIG_PREFIX + VERSION_ID + ctxSuffix);
        l.add(new MOInfo(parseOID(oid), version));
      }
      else {
        break;
      }
    }
    return new OIDInfo(ctx, l.size(), l.iterator());
  }

  private OID parseOID(String oid) {
    try {
      return new OID(oidTextFormat.parse(oid));
    }
    catch (ParseException pex) {
      throw new RuntimeException("OID '"+oid+"' cannot be parsed", pex);
    }
  }

  private void scanDataVariable(SortedMap properties, OID oid, DataInfo<Variable> dataInfo) {
    String prefix = CONFIG_PREFIX;
    String ctxSuffix = "";
    if (dataInfo.context != null) {
      ctxSuffix = CTX_ID+dataInfo.context;
    }
    prefix += ctxSuffix;
    prefix += OID_ID+oid.toDottedString();
    String value = (String) properties.get(prefix);
    Variable v = createVariableFromString(value, Variable.class);
    dataInfo.add(v);
  }

  private void scanDataIndexVariables(SortedMap properties, OID oid, DataInfo<IndexedVariables> dataInfo) {
    String prefix = CONFIG_PREFIX;
    String ctxSuffix = "";
    if (dataInfo.context != null) {
      ctxSuffix = CTX_ID+dataInfo.context;
    }
    prefix += ctxSuffix;
    prefix += OID_ID+oid.toDottedString();
    String dimension = (String) properties.get(prefix);
    if (dimension == null) {
      return;
    }
    StringTokenizer st = new StringTokenizer(dimension, ": ");
    int rows = Integer.parseInt(st.nextToken());
    int cols = Integer.parseInt(st.nextToken());
    String indexPrefix = CONFIG_PREFIX+ctxSuffix+INDEX_ID+oid.toDottedString();
    String valuePrefix = CONFIG_PREFIX+ctxSuffix+VALUE_ID+oid.toDottedString();
    for (int i=0; i<rows; i++) {
      String indexString = (String) properties.get(indexPrefix+"."+i);
      OID index = (OID) createVariableFromString(indexString, OID.class);
      Variable[] v = new Variable[cols];
      for (int j=0; j<cols; j++) {
        String val = (String) properties.get(valuePrefix+"."+i+"."+j);
        if (val != null) {
          v[j] = createVariableFromString(val, Variable.class);
        }
      }
      dataInfo.add(new IndexedVariables(index, v));
    }
  }

  /**
   * Scans the supplied properties for config relevant properties and stores
   * them into the returned LinkedHashMap.
   *
   * @param props
   *    a set of properties.
   * @return
   *    a LinkedHashMap with properties whose key starts with
   *    {@link #CONFIG_PREFIX}.
   */
  private static SortedMap<String,String> scanProperties(Properties props) {
    SortedMap<String,String> map = new TreeMap<String,String>();
    for (Entry<Object, Object> objectObjectEntry : props.entrySet()) {
      Entry e = (Entry) objectObjectEntry;
      if (e.getKey().toString().startsWith(CONFIG_PREFIX)) {
        map.put(e.getKey().toString(), e.getValue().toString());
      }
    }
    return map;
  }

  /**
   * Returns the update mode, which might be one of the constants defined by
   * {@link ImportModes}. By default, {@link ImportModes#REPLACE_CREATE} is
   * returned.
   *
   * @return the constant denoting the update mode that should be used by a
   *   <code>SerializableManagedObject</code> to import its content from
   *   persistent storage.
   */
  public int getImportMode() {
    return importMode;
  }

  public Context readContext() throws IOException {
    if ((state <= STATE_CTX_SEQ) && (contexts != null)) {
      if (state < STATE_CTX_SEQ) {
        state = STATE_CTX_SEQ;
      }
      return (Context) contexts.iterator.next();
    }
    else {
      throw new IOException();
    }
  }

  public IndexedVariables readIndexedVariables() throws IOException {
    if (data == null) {
      DataInfo<IndexedVariables> indexedVariablesDataInfo = new DataInfo<IndexedVariables>(oids.curContext);
      data = indexedVariablesDataInfo;
      scanDataIndexVariables(properties, oids.curOID.getOID(), indexedVariablesDataInfo);
    }
    IndexedVariables ivar = (IndexedVariables) data.next();
    if (logger.isDebugEnabled()) {
      logger.debug("Read indexed variables "+ivar+" for OID "+
                   oids.curOID.getOID()+" in context "+oids.curContext);
    }
    return ivar;
  }

  public MOInfo readManagedObject() throws IOException {
    MOInfo info = (MOInfo) oids.next();
    data = null;
    if (logger.isDebugEnabled()) {
      logger.debug("Read MO "+info);
    }
    return info;
  }

  public Sequence readSequence() throws IOException {
    switch (state) {
      case STATE_ALL_CTX_DATA_SEQ: {
        state++;
        oids = scanOIDs(properties, null);
        return new Sequence(oids.numElements);
      }
      case STATE_ALL_CTX_DATA:
//        state++;
        if (data != null && !oids.iterator.hasNext()) {
          state++;
        }
        else {
          DataInfo<IndexedVariables> indexedVariablesDataInfo = new DataInfo<IndexedVariables>(oids.curContext);
          data = indexedVariablesDataInfo;
          scanDataIndexVariables(properties, oids.curOID.getOID(), indexedVariablesDataInfo);
          return new Sequence(data.size());
        }
      case STATE_CTX_SEQ: {
        state++;
        return new Sequence(contexts.numContexts);
      }
    }
    return null;
  }

  public Variable readVariable() throws IOException {
    if (data == null) {
      DataInfo<Variable> variableDataInfo = new DataInfo<Variable>(oids.curContext);
      data = variableDataInfo;
      scanDataVariable(properties, oids.curOID.getOID(), variableDataInfo);
    }
    Variable v =(Variable) data.next();
    if (logger.isDebugEnabled()) {
      logger.debug("Read variable "+v+" for OID "+oids.curOID.getOID()+
                   " in context "+oids.curContext);
    }
    return v;
  }

  public void skipContext(Context context) throws IOException {

  }

  public void skipManagedObject(MOInfo mo) throws IOException {
  }

  /**
   * Parses a string of the format
   * <pre>
   * OID={type}value where &lt;type&gt; is one of
   * the following single characters enclosed by '{' and '}':
   *  i                     Integer32
   *  u                     UnsingedInteger32, Gauge32
   *  s                     OCTET STRING
   *  x                     OCTET STRING specified as hex string where
   *                        bytes separated by colons (':').
   *  d                     OCTET STRING specified as decimal string
   *                        where bytes are separated by dots ('.').
   *  n                     Null
   *  o                     OBJECT IDENTIFIER
   *  t                     TimeTicks
   *  a                     IpAddress
   *  b                     OCTET STRING specified as binary string where
   *                        bytes are separated by spaces.
   *  $&lt;variableName&gt;     where <variableName> is the name of a predefined
   *                        variable or the OID of a variable of the agent's
   *                        MIB.
   * </pre>
   * and returns the corresponding variable.
   *
   * @param value
   *    the variable value string.
   * @param returnType
   *    the expected Variable class to return.
   * @return
   *    <code>null</code> if <code>value</code> is <code>null</code> and
   *    the <code>Variable</code> corresponding to <code>value</code> otherwise.
   */
  public Variable createVariableFromString(String value, Class<? extends Variable> returnType) {
    if (value != null) {
      char type = ' ';
      String varName = null;
      if (value.length() >= 3) {
        type = value.charAt(1);
        int pos = value.indexOf('}');
        if (type == '$') {
          varName = value.substring(2, pos);
        }
        value = value.substring(pos+1);
      }
      Variable variable;
      switch (type) {
        case 'i':
          variable = new Integer32(Integer.parseInt(value));
          break;
        case 'u':
          variable = new UnsignedInteger32(Long.parseLong(value));
          break;
        case 's':
          variable = new OctetString(value);
          break;
        case 'x':
          variable = OctetString.fromString(value, ':', 16);
          break;
        case 'd':
          variable = OctetString.fromString(value, '.', 10);
          break;
        case 'b':
          variable = OctetString.fromString(value, ' ', 2);
          break;
        case 'n':
          variable = new Null();
          break;
        case 'o':
          try {
            variable = parseOID(value);
          }
          catch (Exception ex) {
            // does oid contain variable reference?
            Pattern p = Pattern.compile("(\\$#?\\{[^\\}]*\\})");
            Matcher m = p.matcher(value);
            StringBuffer result = new StringBuffer();
            while (m.find()) {
              String group = m.group();
              boolean impliedLength = true;
              if (group.charAt(1) == '#') {
                impliedLength = false;
              }
              group = group.substring(group.indexOf('{')+1, group.length()-1);
              Variable replacementValue = (this.variables == null) ?
                  new OID() : this.variables.getVariable(group);
              if (replacementValue != null) {
                OID oid = replacementValue.toSubIndex(impliedLength);
                m.appendReplacement(result, oid.toDottedString());
              }
            }
            m.appendTail(result);
            variable = parseOID(result.toString());
          }
          break;
        case 't':
          variable = new TimeTicks(Long.parseLong(value));
          break;
        case 'a':
          variable = new IpAddress(value);
          break;
        case '$':
          variable = (this.variables == null) ?
              null : this.variables.getVariable(varName);
          break;
        case ' ':
          return null;
        default:
          throw new IllegalArgumentException("Variable type "+type+
                                             " not supported");
       }
       if (!returnType.isInstance(variable)) {
         /**@todo make conversion*/
       }
       return variable;
     }
     return null;
  }

  public void close() throws IOException {
  }

  private class ContextInfo implements Iterator {
    int numContexts;
    OctetString curContext;
    Iterator iterator;

    ContextInfo(int numContexts, Iterator contexts) {
      this.numContexts = numContexts;
      this.iterator = contexts;
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }

    public boolean hasNext() {
      return iterator.hasNext();
    }

    public Object next() {
      curContext = (OctetString) iterator.next();
      return curContext;
    }
  }

  private class OIDInfo implements Iterator {
    int numElements;
    MOInfo curOID;
    OctetString curContext;
    Iterator<MOInfo> iterator;

    OIDInfo(OctetString context, int numElements, Iterator<MOInfo> data) {
      this.numElements = numElements;
      this.iterator = data;
      this.curContext = context;
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }

    public boolean hasNext() {
      return iterator.hasNext();
    }

    public Object next() {
      curOID = iterator.next();
      return curOID;
    }
  }

  private class DataInfo<T> implements Iterator {
    OctetString context;
    List<T> data = new ArrayList<T>();
    Iterator<T> iterator;

    DataInfo(OctetString context) {
      this.context = context;
    }

    public void add(T element) {
      data.add(element);
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }

    private void initIterator() {
      if (iterator == null) {
        iterator = data.iterator();
      }
    }

    public boolean hasNext() {
      initIterator();
      return iterator.hasNext();
    }

    public T next() {
      initIterator();
      return iterator.next();
    }

    public int size() {
      return data.size();
    }
  }

}
