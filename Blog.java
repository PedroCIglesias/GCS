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

  private Retorno<Boolean> addUsuario(Usuario u){ //adiciona usuario 'u'
    if(rN.usuarioExistente(u)){
      return new Retorno<Boolean>(false,"Usuário já existente!");
    }
    usuarios.add(u);
    return new Retorno<Boolean>(true,"Usuário cadastrado!");
  }

  private Retorno<Boolean> addPostagem(Postagem p){ //adiciona publicacao 'p'
    if(p.getConteudo().equals("")){
      return new Retorno<Boolean>(false,"Conteúdo em branco!");
    }
    if(rN.procurarPalavrasProibidas(p.getConteudo())){
      return new Retorno<Boolean>(false,"Palavra proibida!");
    }
    postagens.add(p);
    return new Retorno<Boolean>(true,"Publicado com sucesso!");
  }

  private Retorno<Boolean> addComentario(Postagem post, Comentario c){ //adiciona comentario 'c' na publicacao 'post'
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

  private Retorno<Boolean> removePostagem(Postagem p) { //remove publicacao 'p'
    if(!(userAtual.ehAdmin() || p.getAutor().getId()==userAtual.getId())){
      return new Retorno<Boolean>(false,"Não possui permição para exclusão dessa publicação");
    }
    int index = indexPostagem(p);
    if(index!=-1){
      postagens.remove(index);
      return new Retorno<Boolean>(true,"Publicação removida com sucesso!");
    }
    return new Retorno<Boolean>(false,"Publicação não encontrada!");
  }

  private Retorno<Boolean> removeComentario(Postagem p, Comentario c) { //remove comentario 'c' de uma publicacao 'p'
    if(!(userAtual.ehAdmin() || c.getAutor().getId()==userAtual.getId())){
      return new Retorno<Boolean>(false,"Não possui permição para exclusão desse comentário");
    }
    int index = indexPostagem(p);
    if(index!=-1){
      ArrayList<Comentario> comentariosAux = postagens.get(index).getComentarios();
      for(int i=0;i<=comentariosAux.size()-1;i++){
        if(comentariosAux.get(i).getAutor()==c.getAutor() && comentariosAux.get(i).getDataPostagem()==c.getDataPostagem() && comentariosAux.get(i).getConteudo()==c.getConteudo()){
          postagens.get(index).getComentarios().remove(i);
          return new Retorno<Boolean>(true,"Comentário removido com sucesso!");
        }
      }
    }
    return new Retorno<Boolean>(false,"Comentário não encontrado!");
  }

  private int indexPostagem(Postagem p){ //busca index de uma postagem p no arraylist
    for(int i=0; i<=postagens.size()-1; i++){
      Postagem postAux = postagens.get(i);
      if(postAux.getAutor()==p.getAutor() && postAux.getDataPostagem()==p.getDataPostagem() && postAux.getConteudo()==p.getConteudo()){
        return i;
      }
    }
    return -1;
  }

  private void escreveCSV(){ //registra em CSV
    ArrayList<Postagem> postagensUser = buscaPostagens(userAtual).retorno;
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

  private Retorno<ArrayList<Postagem>> buscaPostagens(Usuario user){ //busca postagens do usuario
    ArrayList<Postagem> postagensUser = new ArrayList<Postagem>();
    for(Postagem p: postagens){
      if(p.getAutor().getId()==user.getId()){
        postagensUser.add(p);
      }
    }
    return new Retorno<ArrayList<Postagem>>(true,"Consultado com sucesso!",postagensUser);
  }

  private Retorno<ArrayList<Postagem>> buscaPostagens(String chave){ //busca postagem por palavra chave
    if(rN.procurarPalavrasProibidas(chave)){
      return new Retorno<ArrayList<Postagem>>(false,"Palavra proibida!");
    }
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
    return new Retorno<ArrayList<Postagem>>(true,"Consultado com sucesso!",postagensPalavraChave);
  }

  private Retorno<ArrayList<Comentario>> buscaComentarios(String chave){//busca comentarios por palavra chave
    if(rN.procurarPalavrasProibidas(chave)){
      return new Retorno<ArrayList<Comentario>>(false,"Palavra proibida!");
    }
    ArrayList<Comentario> comentariosPalavraChave = new ArrayList<Comentario>();
    for(Postagem p: postagens){
      for(Comentario c: p.getComentarios()){
        if(c.getConteudo().toLowerCase().contains(chave.toLowerCase())){
          comentariosPalavraChave.add(c);
        }
      }
    }
    return new Retorno<ArrayList<Comentario>>(true,"Consultado com sucesso!",comentariosPalavraChave);
  }

  private void listaPostagens(){ //printa postagens e seus comentarios
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
  public Retorno<Boolean> criacaoPalavrasProividas (String palavraProibida) {
      if(rN.palavraProibidaExistente(palavraProibida)){
        return new Retorno<Boolean>(false,"Não foi possível cadastrar, palavra já existente");
      }
			palavrasProibidas.add(palavraProibida);
      return new Retorno<Boolean>(true,"Palavra proibida cadastrada com sucesso!");
	}

  public Retorno<Boolean> editaPalavrasProibidas(String nova, String antiga){
    for(int i = 0; i < palavrasProibidas.size(); i++) {
      if(antiga.equalsIgnoreCase(palavrasProibidas.get(i))){
        palavrasProibidas.set(i, nova);
        return new Retorno<Boolean>(true,"Palavra editada com sucesso!");
      }
    }
    return new Retorno<Boolean>(false,"Palavra não encontrada!");
  }
  
  public int totalPosts(){
    return postagens.size();
  }
  public int totalUsuarios(){
    return usuarios.size();
  }
  public int totalComentarios(){
    int totalComentarios=0;
    for(Postagem p:postagens){
      totalComentarios += p.getComentarios().size();
    }
    return totalComentarios;
  }
  public Usuario[] usuariosMaisPostagens(){
    Usuario[] ranking = new Usuario[5];
    int[] qtdPostagem = new int[5];
    int countPostagem =0;
    for(Usuario u: usuarios){
      for(Postagem p: postagens){
        if(u.getId()==p.getAutor().getId()){
          countPostagem++;
        }
        countPostagem = 0;
      }
    }
    return null;
  }
}
