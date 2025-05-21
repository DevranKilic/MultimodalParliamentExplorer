

   
/* Apache UIMA v3 - First created by JCasGen Sun Mar 16 14:35:18 CET 2025 */

package types;
 

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;

import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.impl.TypeSystemImpl;
import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;


import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Sun Mar 16 14:35:18 CET 2025
 * XML source: F://Uni//Informatik//PPR//Aufgaben//multimodal_parliament_explorer_mdsf//src//main//resources//desc//type//BundestagsRede.xml
 * @generated */
public class SpeechSection extends Annotation {
 
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static String _TypeName = "types.SpeechSection";
  
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(SpeechSection.class);
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
   
  public final static String _FeatName_value = "value";
  public final static String _FeatName_textType = "textType";
  public final static String _FeatName_speaker = "speaker";


  /* Feature Adjusted Offsets */
  private final static CallSite _FC_value = TypeSystemImpl.createCallSite(SpeechSection.class, "value");
  private final static MethodHandle _FH_value = _FC_value.dynamicInvoker();
  private final static CallSite _FC_textType = TypeSystemImpl.createCallSite(SpeechSection.class, "textType");
  private final static MethodHandle _FH_textType = _FC_textType.dynamicInvoker();
  private final static CallSite _FC_speaker = TypeSystemImpl.createCallSite(SpeechSection.class, "speaker");
  private final static MethodHandle _FH_speaker = _FC_speaker.dynamicInvoker();

   
  /** Never called.  Disable default constructor
   * @generated */
  @Deprecated
  @SuppressWarnings ("deprecation")
  protected SpeechSection() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param casImpl the CAS this Feature Structure belongs to
   * @param type the type of this Feature Structure 
   */
  public SpeechSection(TypeImpl type, CASImpl casImpl) {
    super(type, casImpl);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public SpeechSection(JCas jcas) {
    super(jcas);
    readObject();   
  } 


  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public SpeechSection(JCas jcas, int begin, int end) {
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
  //* Feature: value

  /** getter for value - gets 
   * @generated
   * @return value of the feature 
   */
  public String getValue() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_value));
  }
    
  /** setter for value - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setValue(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_value), v);
  }    
    
   
    
  //*--------------*
  //* Feature: textType

  /** getter for textType - gets 
   * @generated
   * @return value of the feature 
   */
  public String getTextType() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_textType));
  }
    
  /** setter for textType - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setTextType(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_textType), v);
  }    
    
   
    
  //*--------------*
  //* Feature: speaker

  /** getter for speaker - gets 
   * @generated
   * @return value of the feature 
   */
  public Speaker getSpeaker() { 
    return (Speaker)(_getFeatureValueNc(wrapGetIntCatchException(_FH_speaker)));
  }
    
  /** setter for speaker - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setSpeaker(Speaker v) {
    _setFeatureValueNcWj(wrapGetIntCatchException(_FH_speaker), v);
  }    
    
  }

    