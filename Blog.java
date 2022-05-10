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
    for(Postagem p: postagens){
      if(p.getAutor()==post.getAutor() && p.getDataPostagem()==post.getDataPostagem() && p.getConteudo()==post.getConteudo()){
        p.addComentario(c);
        break;
      }
    }
    return new Retorno<Boolean>(true,"Publicado com sucesso!");
  }

  private Retorno<Boolean> removePostagem(Postagem p) {
    if(!(userAtual.ehAdmin() || p.getAutor().getId()==userAtual.getId())){
      return new Retorno<Boolean>(false,"Não possui permição para exclusão dessa publicação");
    }
    for(int i=0; i<=postagens.size(); i++){
     
    }
    //remove postagem
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
}
