package Export;

import Class_MongoDB_Impl.Abgeordneter_MongoDB_Impl;
import Class_MongoDB_Impl.Rede_MongoDB_Impl;
import Class_MongoDB_Impl.Sitzung_MongoDB_Impl;
import Class_MongoDB_Impl.Tagesordnungspunkt_MongoDB_Impl;
//import com.aspose.pdf.LatexLoadOptions;
import com.mongodb.Mongo;
import database.MongoDBHandler;
import org.apache.uima.cas.CASException;
import org.apache.uima.resource.ResourceInitializationException;
import org.bson.Document;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class PdfTex_Impl {
    private MongoDBHandler dbh;
    public PdfTex_Impl(MongoDBHandler dbh){
        this.dbh = dbh;
    }

    /**
     * Defines the preamble of the LaTeX document with all packages and the definition of
     * some special characters which caused issues when compiling with PDFLaTeX.
     * @return
     */
    private StringBuilder initDocument(){
        StringBuilder preambel = new StringBuilder();
        preambel.append("\\documentclass{article}\n");
        preambel.append("\\usepackage[utf8]{inputenc}\n");
        preambel.append("\\usepackage{geometry}\n");
        preambel.append("\\usepackage{xcolor}\n");
        preambel.append("\\usepackage{makecell}\n");
        preambel.append("\\usepackage[T1]{fontenc}\n");
        preambel.append("\\usepackage[utf8]{inputenc}\n");
        preambel.append("\\usepackage{newunicodechar}\n");
        preambel.append("\\newunicodechar{ }{\\,}\n");
        preambel.append("\\geometry{a4paper, margin=1in}\n");
        preambel.append("\\begin{document}\n");
        preambel.append("\\tableofcontents\n");
        preambel.append("\\newpage");
        return preambel;
    }

    /**
     * Closes the LaTeX document.
     * @return
     */
    private StringBuilder endDocument(){
        StringBuilder endDoc = new StringBuilder();
        endDoc.append("\\end{document}");
        return endDoc;
    }

    /**
     * Takes a MongoID of a *single* Sitzung and converts all its Information into LaTeX Code.
     * @param sitz_mongoID
     * @return
     * @throws ResourceInitializationException
     * @throws CASException
     */
    public StringBuilder singleSitzungTex(int sitz_mongoID) throws ResourceInitializationException, CASException {
        Sitzung_MongoDB_Impl sitz = new Sitzung_MongoDB_Impl(dbh, sitz_mongoID);
        int Sitzungsnummer = sitz.getSitzungsnr();
        StringBuilder sitztex = new StringBuilder();
        sitztex.append(initDocument());
        sitztex.append("\\section{Sitzung: " + Sitzungsnummer + "}\n");
        List<Integer> top = sitz.getTagesordnungspunkte_Mongo_Impl();
        for (Integer i : top){
            Tagesordnungspunkt_MongoDB_Impl temp_top = new Tagesordnungspunkt_MongoDB_Impl(dbh, i);
            List<String> temp_rids = temp_top.getRede_IDs_Mongo_Impl();
            for (String id : temp_rids){
                org.bson.Document tdoc = dbh.query_output("Rede", "Rede_ID", id);
                int tID = tdoc.getInteger("MongoID");
                Rede_MongoDB_Impl trede = new Rede_MongoDB_Impl(dbh, tID);
                sitztex.append(trede.toLaTeX());

            }
        }

        sitztex.append(endDocument());

        return sitztex;

    }

    /**
     * Converts all speech information of a single parliament member and converts it into LaTeX code
     * @param abg_mdbID
     * @return
     * @throws ResourceInitializationException
     * @throws CASException
     */
    public StringBuilder abgRedeTex(int abg_mdbID) throws ResourceInitializationException, CASException {
        Abgeordneter_MongoDB_Impl abg = new Abgeordneter_MongoDB_Impl(dbh, abg_mdbID);
        List<Integer> abg_reden = abg.getReden_Mongo_Impl();
        StringBuilder abgTex = new StringBuilder();
        abgTex.append(initDocument());
        abgTex.append("\\section{Reden}");
        for (Integer i : abg_reden){
            Rede_MongoDB_Impl tr = new Rede_MongoDB_Impl(dbh, i);
            abgTex.append(tr.toLaTeX());
        }
        abgTex.append(endDocument());
        return abgTex;
    }

    /**
     * Takes multiple SitzungsIDs and converts them into LaTeX code
     * @param MongoIDs
     * @return
     * @throws ResourceInitializationException
     * @throws CASException
     */
    public StringBuilder multipleSitzungenTex(List<Integer> MongoIDs) throws ResourceInitializationException, CASException {
        StringBuilder sitzTex = new StringBuilder();
        sitzTex.append(initDocument());
        for (Integer id : MongoIDs){
            Sitzung_MongoDB_Impl sitz = new Sitzung_MongoDB_Impl(dbh, id);
            int SNr = sitz.getSitzungsnr();
            sitzTex.append("\\section{Sitzung: " + SNr + "}\n");
            List<Integer> top = sitz.getTagesordnungspunkte_Mongo_Impl();
            for (Integer i : top){
                Tagesordnungspunkt_MongoDB_Impl temp_top = new Tagesordnungspunkt_MongoDB_Impl(dbh, i);
                List<String> temp_rids = temp_top.getRede_IDs_Mongo_Impl();
                for (String rid : temp_rids){
                    Document tdoc = dbh.query_output("Rede", "Rede_ID", rid);
                    int tID = tdoc.getInteger("MongoID");
                    Rede_MongoDB_Impl mRede = new Rede_MongoDB_Impl(dbh, tID);
                    sitzTex.append(mRede.toLaTeX());
                }
            }

        }
        sitzTex.append(endDocument());
        return sitzTex;
    }

    /**
     * Converts a single speech into LaTeX code.
     * Only used for testing purposes
     * @param r_mdbID
     * @return
     * @throws ResourceInitializationException
     * @throws CASException
     */
    public StringBuilder singleRedeToTeX(int r_mdbID) throws ResourceInitializationException, CASException {
        Rede_MongoDB_Impl rede = new Rede_MongoDB_Impl(dbh, r_mdbID);
        StringBuilder srTex = new StringBuilder();
        //srTex.append(initDocument());
        srTex.append(rede.toLaTeX());
        //srTex.append(endDocument());
        return srTex;

    }


    /**
     * Takes LaTeX Code in form of a String and writes the code to a .pdf file.
     * @param latexCode
     * @param outputFilePath
     */
    public void exportLatexToPDF(String latexCode, String outputFilePath) {
        String tempDirPath = "temp_latex_output";
        File tempDir = new File(tempDirPath);
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }

        String texFilePath = tempDirPath + "/temp.tex";
        File texFile = new File(texFilePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(texFile))) {
            writer.write(latexCode);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {

            for (int i = 0; i < 2; i++) {
                ProcessBuilder processBuilder = new ProcessBuilder(
                        "pdflatex", "-interaction=nonstopmode", "-output-directory=" + tempDirPath, texFilePath
                );
                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();

                InputStream inputStream = process.getInputStream();
                BufferedReader outputReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                System.out.println("pdflatex Output (Run " + (i + 1) + "):");
                while ((line = outputReader.readLine()) != null) {
                    System.out.println(line);
                }

                int exitCode = process.waitFor();
                System.out.println("pdflatex exited with code: " + exitCode);
            }

            File generatedPDF = new File(tempDirPath + "/temp.pdf");
            if (generatedPDF.exists()) {
                Files.move(generatedPDF.toPath(), Paths.get(outputFilePath), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("PDF successfully generated at: " + outputFilePath);
            } else {
                System.err.println("PDF generation failed. Check the LaTeX code for errors.");
            }

            deleteDirectory(tempDir);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * Deletes a dictionary used to delete temporary files.
     * @param directory
     */
    private void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        }
    }
}
