package Export;

import Class_MongoDB_Impl.Abgeordneter_MongoDB_Impl;
import Class_MongoDB_Impl.Rede_MongoDB_Impl;
import database.MongoDBHandler;
import org.apache.uima.cas.CASException;
import org.apache.uima.resource.ResourceInitializationException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class XmiExport {
    private MongoDBHandler dbh;
    public XmiExport(MongoDBHandler dbh){
        this.dbh = dbh;
    }

    /**
     * Converts all speech information of a single parliament member into the XMI format
     * @param abg_mdbID
     * @return
     * @throws ResourceInitializationException
     * @throws CASException
     */
    public StringBuilder abgRedeXmi(int abg_mdbID) throws ResourceInitializationException, CASException {
        Abgeordneter_MongoDB_Impl abg = new Abgeordneter_MongoDB_Impl(dbh, abg_mdbID);

        List<Integer> abg_reden = abg.getReden_Mongo_Impl();

        StringBuilder abgXmi = new StringBuilder();
        abgXmi.append(initXmiDocument());

        abgXmi.append("  <types:Speaker");
        abgXmi.append(" xmi:id=\"").append(abg_mdbID).append("\"");
        abgXmi.append(" firstName=\"").append(abg.getVorname()).append("\"");
        abgXmi.append(" lastName=\"").append(abg.getName()).append("\"");
        abgXmi.append(" birthdate=\"").append("").append("\"");
        abgXmi.append(" party=\"").append(abg.getPartei()).append("\"");
        abgXmi.append(">\n");

        abgXmi.append("    <speeches>\n");

        for (Integer i : abg_reden) {
            Rede_MongoDB_Impl tr = new Rede_MongoDB_Impl(dbh, i);
            abgXmi.append(tr.toXmiSpeech());
        }

        abgXmi.append("    </speeches>\n");

        abgXmi.append("  </types:Speaker>\n");

        abgXmi.append(endXmiDocument());

        return abgXmi;
    }

    /**
     * Generates the opening part of the XMI document.
     * @return A String containing the XMI document header.
     */
    private String initXmiDocument() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<xmi:XMI xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:types=\"http:///types.ecore\">\n";
    }

    /**
     * Generates the closing part of the XMI document.
     * @return A String containing the XMI document footer.
     */
    private String endXmiDocument() {
        return "</xmi:XMI>\n";
    }



    public void exportXmiToFile(String xmiString, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(xmiString);
        }
    }

}
