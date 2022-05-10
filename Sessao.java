public class Sessao {
    Usuario user;
    public Sessao(Usuario user){
        this.user = user;
    }

    private boolean addUsuario(Usuario u){
        return false;
      }
    
      private boolean addPostagem(Postagem p){
        return false;
      }
    
      private boolean removePostagem(int posicao) {
        return false;
      }
    
      private void escreveCSV(Usuario user){
        ArrayList<Postagem> postagem = buscaPostagens(user);
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
}
