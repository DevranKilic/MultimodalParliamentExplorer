package JCas_Extraction_Implementations;

import JCas_Extraction_Interfaces.Token_Extraction_Interface;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse dient dazu die linguistischen Merkmale von Token eines JCas auszulesen.
 */
public class Token_Extraction_Impl implements Token_Extraction_Interface {


    private JCas jCas;

    public Token_Extraction_Impl(JCas aCas){
        this.jCas = aCas;
    }


    public List<Token> getAllToken() throws CASException {
        List<Token> allToken = new ArrayList<>();

        System.out.println("Token werden geladen.");
        for (Token token : JCasUtil.select(jCas, Token.class)) {
            /*
            System.out.println("Begin: "+token.getBegin());
            System.out.println("End: "+token.getEnd());
            System.out.println("Text: "+token.getText());

             */

            allToken.add(token);
        }
        return allToken;
    }


    public int getToken_Begin(int number) throws CASException {
        int found = -1;
        int counter = 0;
        for (Token token : JCasUtil.select(jCas, Token.class)) {
            //System.out.println("TokenID: "+token.getId());
            if (counter == number){
                found = token.getBegin();
                return found;
            }
            counter++;
        }
        return found;
    }


    public int getToken_End(int number) throws CASException {
        int found = -1;
        int counter = 0;
        for (Token token : JCasUtil.select(jCas, Token.class)) {
            //System.out.println("TokenID: "+token.getId());
            if (counter == number){
                found = token.getEnd();
                return found;
            }
            counter++;
        }
        return found;
    }

    public String getToken_Text(int number) throws CASException {
        String found = "";
        int counter = 0;
        for (Token token : JCasUtil.select(jCas, Token.class)) {
            //System.out.println("TokenID: "+token.getId());
            if (counter == number){
                found = token.getText();
                return found;
            }
            counter++;
        }
        return found;
    }
}
