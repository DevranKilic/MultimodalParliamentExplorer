

   
/* Apache UIMA v3 - First created by JCasGen Sun Mar 16 14:35:18 CET 2025 */

package types;
 

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;

import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.impl.TypeSystemImpl;
import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;


import org.apache.uima.jcas.cas.AnnotationBase;


/** 
 * Updated by JCasGen Sun Mar 16 14:35:18 CET 2025
 * XML source: F://Uni//Informatik//PPR//Aufgaben//multimodal_parliament_explorer_mdsf//src//main//resources//desc//type//BundestagsRede.xml
 * @generated */
public class Speaker extends AnnotationBase {
 
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static String _TypeName = "types.Speaker";
  
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Speaker.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
 
  /* *******************
   *   Feature Offsets *
   * *******************/ 
   
  public final static String _FeatName_id = "id";
  public final static String _FeatName_firstName = "firstName";
  public final static String _FeatName_lastName = "lastName";
  public final static String _FeatName_birthdate = "birthdate";
  public final static String _FeatName_party = "party";


  /* Feature Adjusted Offsets */
  private final static CallSite _FC_id = TypeSystemImpl.createCallSite(Speaker.class, "id");
  private final static MethodHandle _FH_id = _FC_id.dynamicInvoker();
  private final static CallSite _FC_firstName = TypeSystemImpl.createCallSite(Speaker.class, "firstName");
  private final static MethodHandle _FH_firstName = _FC_firstName.dynamicInvoker();
  private final static CallSite _FC_lastName = TypeSystemImpl.createCallSite(Speaker.class, "lastName");
  private final static MethodHandle _FH_lastName = _FC_lastName.dynamicInvoker();
  private final static CallSite _FC_birthdate = TypeSystemImpl.createCallSite(Speaker.class, "birthdate");
  private final static MethodHandle _FH_birthdate = _FC_birthdate.dynamicInvoker();
  private final static CallSite _FC_party = TypeSystemImpl.createCallSite(Speaker.class, "party");
  private final static MethodHandle _FH_party = _FC_party.dynamicInvoker();

   
  /** Never called.  Disable default constructor
   * @generated */
  @Deprecated
  @SuppressWarnings ("deprecation")
  protected Speaker() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param casImpl the CAS this Feature Structure belongs to
   * @param type the type of this Feature Structure 
   */
  public Speaker(TypeImpl type, CASImpl casImpl) {
    super(type, casImpl);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Speaker(JCas jcas) {
    super(jcas);
    readObject();   
  } 


  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: id

  /** getter for id - gets 
   * @generated
   * @return value of the feature 
   */
  public String getId() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_id));
  }
    
  /** setter for id - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setId(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_id), v);
  }    
    
   
    
  //*--------------*
  //* Feature: firstName

  /** getter for firstName - gets 
   * @generated
   * @return value of the feature 
   */
  public String getFirstName() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_firstName));
  }
    
  /** setter for firstName - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setFirstName(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_firstName), v);
  }    
    
   
    
  //*--------------*
  //* Feature: lastName

  /** getter for lastName - gets 
   * @generated
   * @return value of the feature 
   */
  public String getLastName() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_lastName));
  }
    
  /** setter for lastName - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setLastName(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_lastName), v);
  }    
    
   
    
  //*--------------*
  //* Feature: birthdate

  /** getter for birthdate - gets 
   * @generated
   * @return value of the feature 
   */
  public double getBirthdate() { 
    return _getDoubleValueNc(wrapGetIntCatchException(_FH_birthdate));
  }
    
  /** setter for birthdate - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setBirthdate(double v) {
    _setDoubleValueNfc(wrapGetIntCatchException(_FH_birthdate), v);
  }    
    
   
    
  //*--------------*
  //* Feature: party

  /** getter for party - gets 
   * @generated
   * @return value of the feature 
   */
  public String getParty() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_party));
  }
    
  /** setter for party - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setParty(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_party), v);
  }    
    
  }

    