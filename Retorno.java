public class Retorno<T> {
   public boolean ok;
   public String mensagem;
   public T retorno;
   
   public Retorno(boolean ok, String mensagem, T retorno){
       this.ok = ok;
       this.mensagem = mensagem;
       this.retorno = retorno;
   }
   public Retorno(boolean ok, String mensagem){
    this.ok = ok;
    this.mensagem = mensagem;
    }
}
