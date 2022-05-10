import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Blog {
  protected ArrayList<Usuario> usuarios;
  protected ArrayList<Postagem> postagens;
  protected Usuario userAtual;
  protected ArrayList<String> palavrasProibidas;
  private RegrasNegocio rN;

  public Blog(){
    this.usuarios = new ArrayList<Usuario>();
    this.postagens = new ArrayList<Postagem>();
    this.palavrasProibidas = new ArrayList<String>();
    this.rN = new RegrasNegocio();
  }

  public void inicalizar(){
    //Programa roda aqui
  }

  private Retorno<Boolean> addUsuario(Usuario u){
    if(rN.usuarioExistente(u)){
      return new Retorno<Boolean>(false,"Usuário já existente!");
    }
    usuarios.add(u);
    return new Retorno<Boolean>(true,"Usuário cadastrado!");
  }

  private Retorno<Boolean> addPostagem(Postagem p){
    if(p.getConteudo().equals("")){
      return new Retorno<Boolean>(false,"Conteúdo em branco!");
    }
    if(rN.procurarPalavrasProibidas(p.getConteudo())){
      return new Retorno<Boolean>(false,"Palavra proibida!");
    }
    postagens.add(p);
    return new Retorno<Boolean>(true,"Publicado com sucesso!");
  }

  private Retorno<Boolean> addComentario(Postagem post, Comentario c){
    if(c.getConteudo().equals("")){
      return new Retorno<Boolean>(false,"Conteúdo em branco!");
    }
    if(rN.procurarPalavrasProibidas(c.getConteudo())){
      return new Retorno<Boolean>(false,"Palavra proibida!");
    }
    if(!rN.limiteCaracteres(c.getConteudo())){
      return new Retorno<Boolean>(false,"Estourou o limite de 100 caracteres!");
    }
    for(Postagem p: postagens){
      if(p.getAutor()==post.getAutor() && p.getDataPostagem()==post.getDataPostagem() && p.getConteudo()==post.getConteudo()){
        p.addComentario(c);
        return new Retorno<Boolean>(true,"Publicado com sucesso!");
      }
    }
    return new Retorno<Boolean>(false,"Publicação não encontrada!");
  }

  private Retorno<Boolean> removePostagem(Postagem p) {
    if(!(userAtual.ehAdmin() || p.getAutor().getId()==userAtual.getId())){
      return new Retorno<Boolean>(false,"Não possui permição para exclusão dessa publicação");
    }
    for(int i=0; i<=postagens.size()-1; i++){
      Postagem postAux = postagens.get(i);
      if(postAux.getAutor()==p.getAutor() && postAux.getDataPostagem()==p.getDataPostagem() && postAux.getConteudo()==p.getConteudo()){
        postagens.remove(i);
        return new Retorno<Boolean>(true,"Publicação removida com sucesso!");
      }
    }
    return new Retorno<Boolean>(false,"Publicação não encontrada!");
  }

  private Retorno<Boolean> removeComentario(Postagem p, Comentario c) {
    if(!(userAtual.ehAdmin() || c.getAutor().getId()==userAtual.getId())){
      return new Retorno<Boolean>(false,"Não possui permição para exclusão desse comentário");
    }
    for(int i=0; i<=postagens.size()-1; i++){
      Postagem postAux = postagens.get(i);
      if(postAux.getAutor()==p.getAutor() && postAux.getDataPostagem()==p.getDataPostagem() && postAux.getConteudo()==p.getConteudo()){
        postagens.remove(i);
        return new Retorno<Boolean>(true,"Publicação removida com sucesso!");
      }
    }
    return new Retorno<Boolean>(false,"Publicação não encontrada!");
  }

  private int indexPostagem(Postagem p){
    for(int i=0; i<=postagens.size()-1; i++){
      Postagem postAux = postagens.get(i);
      if(postAux.getAutor()==p.getAutor() && postAux.getDataPostagem()==p.getDataPostagem() && postAux.getConteudo()==p.getConteudo()){
        return i;
      }
    }
    return -1;
  }


  private void escreveCSV(){
    ArrayList<Postagem> postagensUser = buscaPostagens(userAtual);
    try (PrintWriter writer = new PrintWriter(new File(userAtual.getId()+".csv"))) {
        
        StringBuilder sb = new StringBuilder();
        for(Postagem p : postagensUser){
            sb.append(p.toString());
            sb.append('\n');
        }

        writer.write(sb.toString());
        writer.close();

    } catch (FileNotFoundException e) {
        System.out.println(e.getMessage());
    }
  }

  private ArrayList<Postagem> buscaPostagens(Usuario user){
    ArrayList<Postagem> postagensUser = new ArrayList<Postagem>();
    for(Postagem p: postagens){
      if(p.getAutor().getId()==user.getId()){
        postagensUser.add(p);
      }
    }
    return postagensUser;
  }

  private ArrayList<Postagem> buscaPostagens(String chave){
    ArrayList<Postagem> postagensPalavraChave = new ArrayList<Postagem>();
    for(Postagem p: postagens){
      if(p.getConteudo().toLowerCase().contains(chave.toLowerCase())){
        postagensPalavraChave.add(p);
      }else{
        for(String s: p.getTags()){
          if(s.equalsIgnoreCase(chave)){
            postagensPalavraChave.add(p);
          }
        }
      }
    }
    return postagensPalavraChave;
  }

  private ArrayList<Comentario> buscaComentarios(String chave){
    ArrayList<Comentario> comentariosPalavraChave = new ArrayList<Comentario>();
    for(Postagem p: postagens){
      for(Comentario c: p.getComentarios()){
        if(c.getConteudo().toLowerCase().contains(chave.toLowerCase())){
          comentariosPalavraChave.add(c);
        }
      }
    }
    return comentariosPalavraChave;
  }

  private void listaPostagens(){
    for(int i=postagens.size()-1;i>=0;i--){
      System.out.println(postagens.get(i).getDataPostagem());
      System.out.println("\n");
      System.out.println(postagens.get(i).getAutor().getNome()+"("+ postagens.get(i).getAutor().getId() +")");
      System.out.println("\n");
      System.out.println(postagens.get(i).getConteudo());
      System.out.println("\n");
      for(int j=0; j<=postagens.get(i).getTags().size()-1; j++){
        System.out.println(postagens.get(i).getTags().get(j)+ ", ");
      }
      if(!postagens.get(i).getComentarios().isEmpty()){
        System.out.println("\n ------ Comentários ------");
        for(int h=postagens.get(i).getComentarios().size()-1;h>=0;h--){
          System.out.println(postagens.get(i).getComentarios().get(h).getDataPostagem());
          System.out.println("\n");
          System.out.println(postagens.get(i).getComentarios().get(h).getAutor().getNome()+"("+ postagens.get(i).getComentarios().get(h).getAutor().getId() +")");
          System.out.println("\n");
          System.out.println(postagens.get(i).getComentarios().get(h).getConteudo());
          System.out.println("\n");
        }
      }
    }
  }
}
