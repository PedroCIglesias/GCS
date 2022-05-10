import java.lang.reflect.Constructor;
import java.util.Date;

public class Comentario {
  
  private Usuario autor;
  private String conteudo;
  Date dataPostagem;

  public Comentario(Usuario autor, String conteudo, Date dataPostagem) {
    this.autor = autor;
    this.conteudo = conteudo;
    this.dataPostagem = dataPostagem;
  }

  public Usuario getAutor() {return autor;}
  public Date getDataPostagem() {return dataPostagem;}
  public String getConteudo() {return conteudo;}
}
