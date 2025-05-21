

   
/* Apache UIMA v3 - First created by JCasGen Sun Mar 16 14:35:18 CET 2025 */

package types;
 

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;

import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.impl.TypeSystemImpl;
import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;


import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Sun Mar 16 14:35:18 CET 2025
 * XML source: F://Uni//Informatik//PPR//Aufgaben//multimodal_parliament_explorer_mdsf//src//main//resources//desc//type//BundestagsRede.xml
 * @generated */
public class Speech extends Annotation {
 
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static String _TypeName = "types.Speech";
  
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Speech.class);
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
   
  public final static String _FeatName_speechSections = "speechSections";
  public final static String _FeatName_id = "id";


  /* Feature Adjusted Offsets */
  private final static CallSite _FC_speechSections = TypeSystemImpl.createCallSite(Speech.class, "speechSections");
  private final static MethodHandle _FH_speechSections = _FC_speechSections.dynamicInvoker();
  private final static CallSite _FC_id = TypeSystemImpl.createCallSite(Speech.class, "id");
  private final static MethodHandle _FH_id = _FC_id.dynamicInvoker();

   
  /** Never called.  Disable default constructor
   * @generated */
  @Deprecated
  @SuppressWarnings ("deprecation")
  protected Speech() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param casImpl the CAS this Feature Structure belongs to
   * @param type the type of this Feature Structure 
   */
  public Speech(TypeImpl type, CASImpl casImpl) {
    super(type, casImpl);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Speech(JCas jcas) {
    super(jcas);
    readObject();   
  } 


  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public Speech(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
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
  //* Feature: speechSections

  /** getter for speechSections - gets 
   * @generated
   * @return value of the feature 
   */
  @SuppressWarnings("unchecked")
  public FSList<SpeechSection> getSpeechSections() { 
    return (FSList<SpeechSection>)(_getFeatureValueNc(wrapGetIntCatchException(_FH_speechSections)));
  }
    
  /** setter for speechSections - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setSpeechSections(FSList<SpeechSection> v) {
    _setFeatureValueNcWj(wrapGetIntCatchException(_FH_speechSections), v);
  }    
    
   
    
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
    
  }

    