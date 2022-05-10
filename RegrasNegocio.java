public class RegrasNegocio extends Blog{
    public boolean usuarioExistente(Usuario userNovo){
        for(Usuario u: usuarios){
            if(u.getId()==userNovo.getId());
                return true;
        }
        return false;
    }
    public boolean procurarPalavrasProibidas (String post) {
		String tmpPoString = post.toUpperCase();
		for(int i = 0; i < palavrasProibidas.size(); i++) {
			palavrasProibidas.set(i, palavrasProibidas.get(i).toUpperCase());
		}
		
		for(String tmpList : palavrasProibidas) {
			char char1 [] = new char [tmpPoString.length()];
	        char char2 [] = new char [tmpList.length()];
	        
	        for(int i = 0; i < tmpPoString.length(); i++){
	            char1[i] = tmpPoString.charAt(i);
	        }

	        for(int i = 0; i < tmpList.length(); i++){
	            char2[i] = tmpList.charAt(i);
	        }

	        for(int i = 0; i < char1.length; i++){
	            if(char2[0] == char1[i]){
	                for(int j = 1; j < char2.length; j++){
	                    if(char1[i+j] == char2[j]){
	                        if(j == char2.length-1){
	                        	System.out.println("Palavra Proibida Encontrada");
	                            return true;
	                        }
	                    }else{
	                        break;
	                    }
	                }
	            }
	            
	        }
		}
		return false;
	}
}