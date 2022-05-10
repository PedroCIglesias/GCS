public class Usuario {

  private int id;
  private String nome;
  private boolean administrador;

  Usuario(int id, String nome, Boolean administrador){
    this.id = id;
    this.nome = nome;
    this.administrador = administrador;
  }

  public int getId() {return id;}
  public String getNome() {return nome;}

  public boolean isAdministrador(){
    return this.administrador;
  }
}
