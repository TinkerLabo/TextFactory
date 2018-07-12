package kyPkg.uCodecs;
public class CodeCnv{
	//---+---------+---------+---------+---------+---------+---------+---------+
	// ��������Œ蒷�ɂ��鏈���E�E�E�S�p�ɂ���Ƃ����p�ɂ���Ƃ��͂ǂ����悤���H
	// �g�p��  CodeCnv.fixStr("����������",3); �Ƃ����"������"���Ԃ�E�E�\�肾��
	//---+---------+---------+---------+---------+---------+---------+---------+
	public static String fixStr(String pStr,int pLen){
		String wRtn = "";
		if (pStr.length() < pLen){
			StringBuffer wSbuf = new StringBuffer();
			wSbuf.append(pStr);
			for(int i = pStr.length();i < pLen;i++){
				wSbuf.append(" ");
			}
			wRtn = wSbuf.toString();
		} else {
			wRtn = pStr.substring(0,pLen);
		}
		return wRtn;
	}
	//---+---------+---------+---------+---------+---------+---------+---------+
	// ������ϊ��ȂǂȂ�
	//---+---------+---------+---------+---------+---------+---------+---------+
	/** ���p���������񂩂ǂ���
	*  @param	pStr	����������
	*  @return			���p�����݂̂̕�����Ȃ�true
	*/
	//CodeCnv.isNumeric(String pStr)
	public static boolean isNumeric(String pStr){
//		System.out.println("isNumeric in :" + pStr);
		boolean wRtn = false;
		if (pStr.matches("[+-]*\\d+[.]*\\d*")) {
			//System.out.println("�`�F�b�N���ʁF���p�����݂̂̕�����ł��B");
			wRtn = true;
		} else {
			//System.out.println("�`�F�b�N���ʁF���p�����ȊO�̕������܂܂�܂��B");
			wRtn = false;
		}
//		System.out.println("isNumeric Ans:" + wRtn);
		return wRtn;
	}

	//---+---------+---------+---------+---------+---------+---------+---------+
	/** ���p�p���������񂩂ǂ���
	*  @param	pStr	����������
	*  @return			���p�����݂̂̕�����Ȃ�true
	*/
	public static boolean isAlphaNum(String pStr){
		System.out.println("isNarrow in :" + pStr);
		boolean wRtn = false;
		if (pStr.matches("[0-9a-zA-Z]+")) {
			System.out.println("�`�F�b�N���ʁF���p�p���݂̂̕�����ł��B");
			wRtn = true;
		} else {
			System.out.println("�`�F�b�N���ʁF���p�p���ȊO�̕������܂܂�܂��B");
			wRtn = false;
		}
		System.out.println("isNarrow Ans:" + wRtn);
		return wRtn;
	}
	//---+---------+---------+---------+---------+---------+---------+---------+
	/** �X�g�����O���̔��p������S�p�ɕϊ�
	*  @param	pChar	���͕����i���p ������j
	*  @return			�o�͕����i�S�p ������j
	*  �s�g�p��t
	*  	String wCnv = cnvWide("���$#123��������abcdefgABC");
	*  	System.out.println("After=>" + wCnv );
	*/
	public static String cnvWide(String pStr){
		char[] cArray = pStr.toCharArray();
		for(int i = 0;i<pStr.length();i++){
			char wWide = cnvWide(cArray[i]);
			System.out.println("in:"+cArray[i]+" => out:"+wWide);
			cArray[i] = wWide;
		}
		return new String(cArray);
	}
	//---+---------+---------+---------+---------+---------+---------+---------+
	/** �X�g�����O���̑S�p�����𔼊p�ɕϊ�
	*  @param	pChar	���͕����i�S�p ������j
	*  @return			�o�͕����i���p ������j
	*/
	public static String cnvNarrow(String pStr){
		char[] cArray = pStr.toCharArray();
		for(int i = 0;i<pStr.length();i++){
			char wWide = cnvNarrow(cArray[i]);
			System.out.println("in:"+cArray[i]+" => out:"+wWide);
			cArray[i] = wWide;
		}
		return new String(cArray);
	}

	//---+---------+---------+---------+---------+---------+---------+---------+
	/** �t�@�C�����Ɏg���Ȃ�������ϊ�����
	*  ���^�F���H�h�����b�S�p�ɂ����Ȃ�̖����Ȃ����I�I�i�g���q�͕ʁj
	*  @param	pChar	���͕���
	*  @return			�o�͕���
	*/
	public static char cnvNGfnm(char pChar){
		char wNG =  ' ';
		switch(pChar){
			case'\\' : wNG = '_' ;break;
			case'/'  : wNG = '_' ;break;
			case':'  : wNG = '_' ;break;
			case'*'  : wNG = '_' ;break;
			case'?'  : wNG = '_' ;break;
			case'\"' : wNG = '_' ;break;
			case'<'  : wNG = '_' ;break;
			case'>'  : wNG = '_' ;break;
			case'|'  : wNG = '_' ;break;
		}
		return wNG;
	}
	//---+---------+---------+---------+---------+---------+---------+---------+
	/** �G�N�Z���̃V�[�g���ȂǂŎg���Ȃ�������ϊ�����
	*  @param	pChar	���͕���
	*  @return			�o�͕���
	*/
	public static char cnvNGfExcel(char pChar){
		char wNG =  ' ';
		switch(pChar){
			case'\\' : wNG = '_' ;break;
			case'/'  : wNG = '_' ;break;
			case':'  : wNG = '_' ;break;
			case'*'  : wNG = '_' ;break;
			case'?'  : wNG = '_' ;break;
			case'\"' : wNG = '_' ;break;
			case'<'  : wNG = '_' ;break;
			case'>'  : wNG = '_' ;break;
			case'|'  : wNG = '_' ;break;
		}
		return wNG;
	}
	//---+---------+---------+---------+---------+---------+---------+---------+
	public static void main(String[] argv){
		 new CodeCnv();
	}
	//---+---------+---------+---------+---------+---------+---------+---------+
	/** �L�����N�^���p������S�p�ɕϊ�
	*  @param	pChar	���͕����i���p �L�����N�^�j
	*  @return			�o�͕����i�S�p �L�����N�^�j
	*/
	public static char cnvWide(char pChar){
		char wWide = '��';
		switch(pChar){
			case ' '  : wWide = '�@';break;
			case '!'  : wWide = '�I';break;
			case '"'  : wWide = '�h';break;
			case '#'  : wWide = '��';break;
			case '$'  : wWide = '��';break;
			case '%'  : wWide = '��';break;
			case '&'  : wWide = '��';break;
			case '\'' : wWide = '�f';break;
			case '('  : wWide = '�i';break;
			case ')'  : wWide = '�j';break;
			case '*'  : wWide = '��';break;
			case '+'  : wWide = '�{';break;
			case ','  : wWide = '�C';break;
//			case '-'  : wWide = '�|';break;
			case '.'  : wWide = '�D';break;
			case '/'  : wWide = '�^';break;
			case '0'  : wWide = '�O';break;
			case '1'  : wWide = '�P';break;
			case '2'  : wWide = '�Q';break;
			case '3'  : wWide = '�R';break;
			case '4'  : wWide = '�S';break;
			case '5'  : wWide = '�T';break;
			case '6'  : wWide = '�U';break;
			case '7'  : wWide = '�V';break;
			case '8'  : wWide = '�W';break;
			case '9'  : wWide = '�X';break;
			case ':'  : wWide = '�F';break;
			case ';'  : wWide = '�G';break;
			case '<'  : wWide = '��';break;
			case '='  : wWide = '��';break;
			case '>'  : wWide = '��';break;
			case '?'  : wWide = '�H';break;
			case '@'  : wWide = '��';break;
			case 'A'  : wWide = '�`';break;
			case 'B'  : wWide = '�a';break;
			case 'C'  : wWide = '�b';break;
			case 'D'  : wWide = '�c';break;
			case 'E'  : wWide = '�d';break;
			case 'F'  : wWide = '�e';break;
			case 'G'  : wWide = '�f';break;
			case 'H'  : wWide = '�g';break;
			case 'I'  : wWide = '�h';break;
			case 'J'  : wWide = '�i';break;
			case 'K'  : wWide = '�j';break;
			case 'L'  : wWide = '�k';break;
			case 'M'  : wWide = '�l';break;
			case 'N'  : wWide = '�m';break;
			case 'O'  : wWide = '�n';break;
			case 'P'  : wWide = '�o';break;
			case 'Q'  : wWide = '�p';break;
			case 'R'  : wWide = '�q';break;
			case 'S'  : wWide = '�r';break;
			case 'T'  : wWide = '�s';break;
			case 'U'  : wWide = '�t';break;
			case 'V'  : wWide = '�u';break;
			case 'W'  : wWide = '�v';break;
			case 'X'  : wWide = '�w';break;
			case 'Y'  : wWide = '�x';break;
			case 'Z'  : wWide = '�y';break;
			case '['  : wWide = '�m';break;
			case '\\' : wWide = '��';break;
			case ']'  : wWide = '�n';break;
			case '^'  : wWide = '�O';break;
			case '_'  : wWide = '�Q';break;
			case '`'  : wWide = '�M';break;
			case 'a'  : wWide = '��';break;
			case 'b'  : wWide = '��';break;
			case 'c'  : wWide = '��';break;
			case 'd'  : wWide = '��';break;
			case 'e'  : wWide = '��';break;
			case 'f'  : wWide = '��';break;
			case 'g'  : wWide = '��';break;
			case 'h'  : wWide = '��';break;
			case 'i'  : wWide = '��';break;
			case 'j'  : wWide = '��';break;
			case 'k'  : wWide = '��';break;
			case 'l'  : wWide = '��';break;
			case 'm'  : wWide = '��';break;
			case 'n'  : wWide = '��';break;
			case 'o'  : wWide = '��';break;
			case 'p'  : wWide = '��';break;
			case 'q'  : wWide = '��';break;
			case 'r'  : wWide = '��';break;
			case 's'  : wWide = '��';break;
			case 't'  : wWide = '��';break;
			case 'u'  : wWide = '��';break;
			case 'v'  : wWide = '��';break;
			case 'w'  : wWide = '��';break;
			case 'x'  : wWide = '��';break;
			case 'y'  : wWide = '��';break;
			case 'z'  : wWide = '��';break;
			case '{'  : wWide = '�o';break;
			case '|'  : wWide = '�b';break;
			case '}'  : wWide = '�p';break;
			case '~'  : wWide = '�P';break;
			case '�'  : wWide = '�B';break;
			case '�'  : wWide = '�u';break;
			case '�'  : wWide = '�v';break;
			case '�'  : wWide = '�A';break;
			case '�'  : wWide = '�E';break;
			case '�'  : wWide = '��';break;
			case '�'  : wWide = '�@';break;
			case '�'  : wWide = '�B';break;
			case '�'  : wWide = '�D';break;
			case '�'  : wWide = '�F';break;
			case '�'  : wWide = '�H';break;
			case '�'  : wWide = '��';break;
			case '�'  : wWide = '��';break;
			case '�'  : wWide = '��';break;
			case '�'  : wWide = '�b';break;
			case '-'  : wWide = '�|';break;
			case '�'  : wWide = '�A';break;
			case '�'  : wWide = '�C';break;
			case '�'  : wWide = '�E';break;
			case '�'  : wWide = '�G';break;
			case '�'  : wWide = '�I';break;
			case '�'  : wWide = '�J';break;
			case '�'  : wWide = '�L';break;
			case '�'  : wWide = '�N';break;
			case '�'  : wWide = '�P';break;
			case '�'  : wWide = '�R';break;
			case '�'  : wWide = '�T';break;
			case '�'  : wWide = '�V';break;
			case '�'  : wWide = '�X';break;
			case '�'  : wWide = '�Z';break;
			case '�'  : wWide = '�\';break;
			case '�'  : wWide = '�^';break;
			case '�'  : wWide = '�`';break;
			case '�'  : wWide = '�c';break;
			case '�'  : wWide = '�e';break;
			case '�'  : wWide = '�g';break;
			case '�'  : wWide = '�i';break;
			case '�'  : wWide = '�j';break;
			case '�'  : wWide = '�k';break;
			case '�'  : wWide = '�l';break;
			case '�'  : wWide = '�m';break;
			case '�'  : wWide = '�n';break;
			case '�'  : wWide = '�q';break;
			case '�'  : wWide = '�t';break;
			case '�'  : wWide = '�w';break;
			case '�'  : wWide = '�z';break;
			case '�'  : wWide = '�}';break;
			case '�'  : wWide = '�~';break;
			case '�'  : wWide = '��';break;
			case '�'  : wWide = '��';break;
			case '�'  : wWide = '��';break;
			case '�'  : wWide = '��';break;
			case '�'  : wWide = '��';break;
			case '�'  : wWide = '��';break;
			case '�'  : wWide = '��';break;
			case '�'  : wWide = '��';break;
			case '�'  : wWide = '��';break;
			case '�'  : wWide = '��';break;
			case '�'  : wWide = '��';break;
			case '�'  : wWide = '��';break;
			case '�'  : wWide = '��';break;
			case '�'  : wWide = '�J';break;
			case '�'  : wWide = '�K';break;
//		  default   : wWide = '��';
		}
		return wWide;
	}
	//---+---------+---------+---------+---------+---------+---------+---------+
	/** �L�����N�^�S�p�����𔼊p�ɕϊ�
	*  @param	pChar	���͕����i�S�p �L�����N�^�j
	*  @return			�o�͕����i���p �L�����N�^�j
	*/
	public static char cnvNarrow(char pChar){
		//�����i�K��U��_��o��4�s�j�A�������͖��Ή��E�E��̒������ς��̂�
		char wNarrow =  '?';
		switch(pChar){
			case'��' : wNarrow = '1' ;break;  //�I�}�P
			case'��' : wNarrow = '2' ;break;  //�I�}�P
			case'�Q' : wNarrow = '3' ;break;  //�I�}�P
			case'��' : wNarrow = '1' ;break;  //�I�}�P
			case'��' : wNarrow = '2' ;break;  //�I�}�P
			case'�O' : wNarrow = '3' ;break;  //�I�}�P
			case'�l' : wNarrow = '4' ;break;  //�I�}�P
			case'��' : wNarrow = '5' ;break;  //�I�}�P
			case'�Z' : wNarrow = '6' ;break;  //�I�}�P
			case'��' : wNarrow = '7' ;break;  //�I�}�P
			case'��' : wNarrow = '8' ;break;  //�I�}�P
			case'��' : wNarrow = '9' ;break;  //�I�}�P
			case'�\' : wNarrow = '0' ;break;  //�I�}�P
			case'�@' : wNarrow = ' ' ;break;
			case'�I' : wNarrow = '!' ;break;
			case'�h' : wNarrow = '"' ;break;
			case'��' : wNarrow = '#' ;break;
			case'��' : wNarrow = '$' ;break;
			case'��' : wNarrow = '%' ;break;
			case'��' : wNarrow = '&' ;break;
			case'�f' : wNarrow = '\'';break;
			case'�i' : wNarrow = '(' ;break;
			case'�j' : wNarrow = ')' ;break;
			case'��' : wNarrow = '*' ;break;
			case'�{' : wNarrow = '+' ;break;
			case'�C' : wNarrow = ',' ;break;
			case'�[' : wNarrow = '-' ;break;
			case'�D' : wNarrow = '.' ;break;
			case'�^' : wNarrow = '/' ;break;
			case'�O' : wNarrow = '0' ;break;
			case'�P' : wNarrow = '1' ;break;
			case'�Q' : wNarrow = '2' ;break;
			case'�R' : wNarrow = '3' ;break;
			case'�S' : wNarrow = '4' ;break;
			case'�T' : wNarrow = '5' ;break;
			case'�U' : wNarrow = '6' ;break;
			case'�V' : wNarrow = '7' ;break;
			case'�W' : wNarrow = '8' ;break;
			case'�X' : wNarrow = '9' ;break;
			case'�F' : wNarrow = ':' ;break;
			case'�G' : wNarrow = ';' ;break;
			case'��' : wNarrow = '<' ;break;
			case'��' : wNarrow = '=' ;break;
			case'��' : wNarrow = '>' ;break;
			case'�H' : wNarrow = '?' ;break;
			case'��' : wNarrow = '@' ;break;
			case'�`' : wNarrow = 'A' ;break;
			case'�a' : wNarrow = 'B' ;break;
			case'�b' : wNarrow = 'C' ;break;
			case'�c' : wNarrow = 'D' ;break;
			case'�d' : wNarrow = 'E' ;break;
			case'�e' : wNarrow = 'F' ;break;
			case'�f' : wNarrow = 'G' ;break;
			case'�g' : wNarrow = 'H' ;break;
			case'�h' : wNarrow = 'I' ;break;
			case'�i' : wNarrow = 'J' ;break;
			case'�j' : wNarrow = 'K' ;break;
			case'�k' : wNarrow = 'L' ;break;
			case'�l' : wNarrow = 'M' ;break;
			case'�m' : wNarrow = 'N' ;break;
			case'�n' : wNarrow = 'O' ;break;
			case'�o' : wNarrow = 'P' ;break;
			case'�p' : wNarrow = 'Q' ;break;
			case'�q' : wNarrow = 'R' ;break;
			case'�r' : wNarrow = 'S' ;break;
			case'�s' : wNarrow = 'T' ;break;
			case'�t' : wNarrow = 'U' ;break;
			case'�u' : wNarrow = 'V' ;break;
			case'�v' : wNarrow = 'W' ;break;
			case'�w' : wNarrow = 'X' ;break;
			case'�x' : wNarrow = 'Y' ;break;
			case'�y' : wNarrow = 'Z' ;break;
			case'�m' : wNarrow = '[' ;break;
			case'��' : wNarrow = '\\';break;
			case'�n' : wNarrow = ']' ;break;
			case'�O' : wNarrow = '^' ;break;
			case'�Q' : wNarrow = '_' ;break;
			case'�M' : wNarrow = '`' ;break;
			case'��' : wNarrow = 'a' ;break;
			case'��' : wNarrow = 'b' ;break;
			case'��' : wNarrow = 'c' ;break;
			case'��' : wNarrow = 'd' ;break;
			case'��' : wNarrow = 'e' ;break;
			case'��' : wNarrow = 'f' ;break;
			case'��' : wNarrow = 'g' ;break;
			case'��' : wNarrow = 'h' ;break;
			case'��' : wNarrow = 'i' ;break;
			case'��' : wNarrow = 'j' ;break;
			case'��' : wNarrow = 'k' ;break;
			case'��' : wNarrow = 'l' ;break;
			case'��' : wNarrow = 'm' ;break;
			case'��' : wNarrow = 'n' ;break;
			case'��' : wNarrow = 'o' ;break;
			case'��' : wNarrow = 'p' ;break;
			case'��' : wNarrow = 'q' ;break;
			case'��' : wNarrow = 'r' ;break;
			case'��' : wNarrow = 's' ;break;
			case'��' : wNarrow = 't' ;break;
			case'��' : wNarrow = 'u' ;break;
			case'��' : wNarrow = 'v' ;break;
			case'��' : wNarrow = 'w' ;break;
			case'��' : wNarrow = 'x' ;break;
			case'��' : wNarrow = 'y' ;break;
			case'��' : wNarrow = 'z' ;break;
			case'�o' : wNarrow = '{' ;break;
			case'�b' : wNarrow = '|' ;break;
			case'�p' : wNarrow = '}' ;break;
			case'�P' : wNarrow = '~' ;break;
			case'�B' : wNarrow = '�' ;break;
			case'�u' : wNarrow = '�' ;break;
			case'�v' : wNarrow = '�' ;break;
			case'�A' : wNarrow = '�' ;break;
			case'�E' : wNarrow = '�' ;break;
			case'��' : wNarrow = '�' ;break;
			case'�@' : wNarrow = '�' ;break;
			case'�B' : wNarrow = '�' ;break;
			case'�D' : wNarrow = '�' ;break;
			case'�F' : wNarrow = '�' ;break;
			case'�H' : wNarrow = '�' ;break;
			case'��' : wNarrow = '�' ;break;
			case'��' : wNarrow = '�' ;break;
			case'��' : wNarrow = '�' ;break;
			case'�b' : wNarrow = '�' ;break;
			case'�|' : wNarrow = '-' ;break;
			case'�A' : wNarrow = '�' ;break;
			case'�C' : wNarrow = '�' ;break;
			case'�E' : wNarrow = '�' ;break;
			case'�G' : wNarrow = '�' ;break;
			case'�I' : wNarrow = '�' ;break;
			case'�J' : wNarrow = '�' ;break;
			case'�L' : wNarrow = '�' ;break;
			case'�N' : wNarrow = '�' ;break;
			case'�P' : wNarrow = '�' ;break;
			case'�R' : wNarrow = '�' ;break;
			case'�T' : wNarrow = '�' ;break;
			case'�V' : wNarrow = '�' ;break;
			case'�X' : wNarrow = '�' ;break;
			case'�Z' : wNarrow = '�' ;break;
			case'�\' : wNarrow = '�' ;break;
			case'�^' : wNarrow = '�' ;break;
			case'�`' : wNarrow = '�' ;break;
			case'�c' : wNarrow = '�' ;break;
			case'�e' : wNarrow = '�' ;break;
			case'�g' : wNarrow = '�' ;break;
			case'�i' : wNarrow = '�' ;break;
			case'�j' : wNarrow = '�' ;break;
			case'�k' : wNarrow = '�' ;break;
			case'�l' : wNarrow = '�' ;break;
			case'�m' : wNarrow = '�' ;break;
			case'�n' : wNarrow = '�' ;break;
			case'�q' : wNarrow = '�' ;break;
			case'�t' : wNarrow = '�' ;break;
			case'�w' : wNarrow = '�' ;break;
			case'�z' : wNarrow = '�' ;break;
			case'�}' : wNarrow = '�' ;break;
			case'�~' : wNarrow = '�' ;break;
			case'��' : wNarrow = '�' ;break;
			case'��' : wNarrow = '�' ;break;
			case'��' : wNarrow = '�' ;break;
			case'��' : wNarrow = '�' ;break;
			case'��' : wNarrow = '�' ;break;
			case'��' : wNarrow = '�' ;break;
			case'��' : wNarrow = '�' ;break;
			case'��' : wNarrow = '�' ;break;
			case'��' : wNarrow = '�' ;break;
			case'��' : wNarrow = '�' ;break;
			case'��' : wNarrow = '�' ;break;
			case'��' : wNarrow = '�' ;break;
			case'��' : wNarrow = '�' ;break;
			case'�J' : wNarrow = '�' ;break;
			case'�K' : wNarrow = '�' ;break;
//		  default  : wNarrow = '';
		}
		return wNarrow;
	}



/*
	�ȉ��R�����g�A�E�g���Ă���܂�
	//---+---------+---------+---------+---------+---------+---------+---------+
	// switch���̑���Ƀn�b�V���}�b�v���g�����@���l���悤�Ƃ�����
	// �i��ʂɕϊ�����K�v������ꍇ�͂��̕��������Ǝv�����j
	// char�̓I�u�W�F�N�g����Ȃ��̂�HashTable�Ŏg�p���邽�߂ɂ�
	// ��U���b�p�[�N���X�ō��Ȃ���΂Ȃ�Ȃ��E�E�E�d�����Ȃ̂�
	// �̂ŁAString�z��Ƃ��悤�Ƃ������A������^�ϊ����p���������Ȃ̂�
	// ���̂܂܃y���f�C���O���Ă���B
	// �����h���h��"��"�̗l�ȕϊ���String�ŏ������Ȃ���΂Ȃ�Ȃ����낤�E�E
	// ���}�b�s���O���镶�����C���R�A���錳�̓e�L�X�g�t�@�C�����Ƃ�������
	//---+---------+---------+---------+---------+---------+---------+---------+
	Object[][] map001 = {
		{"\'","�f"},{"\\","��"},{"\"","�h"},
	    {" ","�@"},{"!","�I"},{"#","��"},{"$","��"},{"%","��"},
		{"&","��"},{"(","�i"},{")","�j"},{"*","��"},{"+","�{"},
		{",","�C"},{".","�D"},{"/","�^"},{"?","�H"},{"@","��"},
		{"0","�O"},{"1","�P"},{"2","�Q"},{"3","�R"},{"4","�S"},
		{"5","�T"},{"6","�U"},{"7","�V"},{"8","�W"},{"9","�X"},
		{":","�F"},{";","�G"},{"<","��"},{"=","��"},{">","��"},
		{"A","�`"},{"B","�a"},{"C","�b"},{"D","�c"},{"E","�d"},
		{"F","�e"},{"G","�f"},{"H","�g"},{"I","�h"},{"J","�i"},
		{"K","�j"},{"L","�k"},{"M","�l"},{"N","�m"},{"O","�n"},
		{"P","�o"},{"Q","�p"},{"R","�q"},{"S","�r"},{"T","�s"},
		{"U","�t"},{"V","�u"},{"W","�v"},{"X","�w"},{"Y","�x"},
		{"Z","�y"},{"[","�m"},{"]","�n"},{"^","�O"},{"_","�Q"},
		{"`","�M"},{"a","��"},{"b","��"},{"c","��"},{"d","��"},
		{"e","��"},{"f","��"},{"g","��"},{"h","��"},{"i","��"},
		{"j","��"},{"k","��"},{"l","��"},{"m","��"},{"n","��"},
		{"o","��"},{"p","��"},{"q","��"},{"r","��"},{"s","��"},
		{"t","��"},{"u","��"},{"v","��"},{"w","��"},{"x","��"},
		{"y","��"},{"z","��"},{"{","�o"},{"|","�b"},{"}","�p"},
		{"~","�P"},{"�","�B"},{"�","�u"},{"�","�v"},{"�","�A"},
		{"�","�E"},{"�","��"},{"�","�@"},{"�","�B"},{"�","�D"},
		{"�","�F"},{"�","�H"},{"�","��"},{"�","��"},{"�","��"},
		{"�","�b"},{"-","�|"},{"�","�A"},{"�","�C"},{"�","�E"},
		{"�","�G"},{"�","�I"},{"�","�J"},{"�","�L"},{"�","�N"},
		{"�","�P"},{"�","�R"},{"�","�T"},{"�","�V"},{"�","�X"},
		{"�","�Z"},{"�","�\"},{"�","�^"},{"�","�`"},{"�","�c"},
		{"�","�e"},{"�","�g"},{"�","�i"},{"�","�j"},{"�","�k"},
		{"�","�l"},{"�","�m"},{"�","�n"},{"�","�q"},{"�","�t"},
		{"�","�w"},{"�","�z"},{"�","�}"},{"�","�~"},{"�","��"},
		{"�","��"},{"�","��"},{"�","��"},{"�","��"},{"�","��"},
		{"�","��"},{"�","��"},{"�","��"},{"�","��"},{"�","��"},
		{"�","��"},{"�","��"},{"�","�J"},{"�","�K"}  
	};
	Object[][] map002 = {
		{"�f","\'"},{"��","\\"},{"�h","\""},
		{"�@"," "},{"�I","!"},{"��","#"},{"��","$"},{"��","%"},
		{"��","&"},{"�i","("},{"�j",")"},{"��","*"},{"�{","+"},
		{"�C",","},{"�[","-"},{"�D","."},{"�^","/"},{"�O","0"},
		{"�P","1"},{"�Q","2"},{"�R","3"},{"�S","4"},{"�T","5"},
		{"�U","6"},{"�V","7"},{"�W","8"},{"�X","9"},{"�F",":"},
		{"�G",";"},{"��","<"},{"��","="},{"��",">"},{"�H","?"},
		{"��","@"},{"�`","A"},{"�a","B"},{"�b","C"},{"�c","D"},
		{"�d","E"},{"�e","F"},{"�f","G"},{"�g","H"},{"�h","I"},
		{"�i","J"},{"�j","K"},{"�k","L"},{"�l","M"},{"�m","N"},
		{"�n","O"},{"�o","P"},{"�p","Q"},{"�q","R"},{"�r","S"},
		{"�s","T"},{"�t","U"},{"�u","V"},{"�v","W"},{"�w","X"},
		{"�x","Y"},{"�y","Z"},{"�m","["},{"�n","]"},{"�O","^"},
		{"�Q","_"},{"�M","`"},{"��","a"},{"��","b"},{"��","c"},
		{"��","d"},{"��","e"},{"��","f"},{"��","g"},{"��","h"},
		{"��","i"},{"��","j"},{"��","k"},{"��","l"},{"��","m"},
		{"��","n"},{"��","o"},{"��","p"},{"��","q"},{"��","r"},
		{"��","s"},{"��","t"},{"��","u"},{"��","v"},{"��","w"},
		{"��","x"},{"��","y"},{"��","z"},{"�o","{"},{"�b","|"},
		{"�p","}"},{"�P","~"},{"�B","�"},{"�u","�"},{"�v","�"},
		{"�A","�"},{"�E","�"},{"��","�"},{"�@","�"},{"�B","�"},
		{"�D","�"},{"�F","�"},{"�H","�"},{"��","�"},{"��","�"},
		{"��","�"},{"�b","�"},{"�|","-"},{"�A","�"},{"�C","�"},
		{"�E","�"},{"�G","�"},{"�I","�"},{"�J","�"},{"�L","�"},
		{"�N","�"},{"�P","�"},{"�R","�"},{"�T","�"},{"�V","�"},
		{"�X","�"},{"�Z","�"},{"�\","�"},{"�^","�"},{"�`","�"},
		{"�c","�"},{"�e","�"},{"�g","�"},{"�i","�"},{"�j","�"},
		{"�k","�"},{"�l","�"},{"�m","�"},{"�n","�"},{"�q","�"},
		{"�t","�"},{"�w","�"},{"�z","�"},{"�}","�"},{"�~","�"},
		{"��","�"},{"��","�"},{"��","�"},{"��","�"},{"��","�"},
		{"��","�"},{"��","�"},{"��","�"},{"��","�"},{"��","�"},
		{"��","�"},{"��","�"},{"��","�"},{"�J","�"},{"�K","�"},
		{"��","1"},{"��","2"},{"�O","3"},{"�l","4"},
		{"��","5"},{"�Z","6"},{"��","7"},{"��","8"},{"��","9"},
		{"�\","0"} //�I�}�P
	};
	HashMap wHash001 = new java.util.HashMap();
	//----------------------------------------------------------------
	public CodeCnv(){
		incore001();
	}
	//----------------------------------------------------------------
	// incore map001���n�b�V���e�[�u����
	//----------------------------------------------------------------
	public void incore001(){
		for(int i = 0;i<map001.length;i++){
			if(map001[i].length==2){
				wHash001.put(map001[i][0],map001[i][1]);
			}
		}
	}
	//----------------------------------------------------------------
	// �ϊ��R�[�h���n�b�V���e�[�u��������o��
	//----------------------------------------------------------------
	public String getNam001(String pKey){
		System.out.println("��getNam001��in�y"+pKey+"�z");
		String wRtn = pKey;
		if ( ! pKey.trim().equals("")){
			Object wObj = wHash001.get(pKey);
			if(wObj!=null){
				if (wObj instanceof String){
					wRtn = wObj.toString();
				}else{
					wRtn = "null?";
				}
			}
		}else{
			wRtn = "?";
		}
		System.out.println("��getNam001��Out�y"+wRtn+"�z");
		return wRtn;
	}
*/

}
