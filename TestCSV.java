import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Date;

public class TestCSV {
    public static void main(String args[]){
        Usuario user = new Usuario(1, "Pedro", true);
        Postagem postagem = new Postagem(user, new Date(), "conteudo", new ArrayList<>());
        ArrayList<Postagem> postagens = new ArrayList<Postagem>();
        postagens.add(postagem);
        postagem = new Postagem(user, new Date(),"conteudo2", new ArrayList<>());
        postagens.add(postagem);
        registraCSV(user,postagens);
    }
    public static void registraCSV(Usuario user,ArrayList<Postagem> postagens){
        try (PrintWriter writer = new PrintWriter(new File(user.getId()+".csv"))) {
            
            StringBuilder sb = new StringBuilder();
            for(Postagem p : postagens){
                sb.append(p.toString());
                sb.append('\n');
            }

            writer.write(sb.toString());
            writer.close();

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        TestCSV testCSV =  new TestCSV();
        testCSV.readCSVFile();
    }

    public void readCSVFile(){
        List<List<String>> records = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("1.csv"));) {
            while (scanner.hasNextLine()) {
                records.add(getRecordFromLine(scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(records.toString());
    }
    private List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<String>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(",");
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }

}