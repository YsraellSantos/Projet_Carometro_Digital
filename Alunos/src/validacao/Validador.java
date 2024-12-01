package validacao;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

@SuppressWarnings("serial")
public class Validador extends PlainDocument{

	private int limiteCaracterres;

	
	public Validador(int limiteCaracterres) {
		super();
		this.limiteCaracterres = limiteCaracterres;
	}
	
	public void insertString(int ofs, String str, AttributeSet a)
	throws BadLocationException{
		if (getLength()+ str.length() <limiteCaracterres) {
			super.insertString(ofs, str, a);
		}
	}

}
	
	

