package kyPkg.uCodecs;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;


public class SBytesTest {
	@Test
	public void testIsNarrow() {
		assertEquals(true, SBytes.isNarrow("abcdefg"));
//		assertEquals(false, SBytes.isNarrow("‚`‚a‚b‚c‚d‚e‚f"));

		assertEquals(false, SBytes.isNarrow('‚ '));
		assertEquals(false, SBytes.isNarrow('‚P'));
		assertEquals(false, SBytes.isNarrow('‚`'));
		
		assertEquals(true, SBytes.isNarrow(' '));
		assertEquals(true, SBytes.isNarrow('!'));
		assertEquals(true, SBytes.isNarrow('"'));
		assertEquals(true, SBytes.isNarrow('#'));
		assertEquals(true, SBytes.isNarrow('$'));
		assertEquals(true, SBytes.isNarrow('%'));
		assertEquals(true, SBytes.isNarrow('&'));
		assertEquals(true, SBytes.isNarrow('\''));
		assertEquals(true, SBytes.isNarrow('('));
		assertEquals(true, SBytes.isNarrow(')'));
		assertEquals(true, SBytes.isNarrow('*'));
		assertEquals(true, SBytes.isNarrow('+'));
		assertEquals(true, SBytes.isNarrow(','));
		assertEquals(true, SBytes.isNarrow('-'));
		assertEquals(true, SBytes.isNarrow('.'));
		assertEquals(true, SBytes.isNarrow('/'));
		assertEquals(true, SBytes.isNarrow('0'));
		assertEquals(true, SBytes.isNarrow('1'));
		assertEquals(true, SBytes.isNarrow('2'));
		assertEquals(true, SBytes.isNarrow('3'));
		assertEquals(true, SBytes.isNarrow('4'));
		assertEquals(true, SBytes.isNarrow('5'));
		assertEquals(true, SBytes.isNarrow('6'));
		assertEquals(true, SBytes.isNarrow('7'));
		assertEquals(true, SBytes.isNarrow('8'));
		assertEquals(true, SBytes.isNarrow('9'));
		assertEquals(true, SBytes.isNarrow(':'));
		assertEquals(true, SBytes.isNarrow(';'));
		assertEquals(true, SBytes.isNarrow('<'));
		assertEquals(true, SBytes.isNarrow('='));
		assertEquals(true, SBytes.isNarrow('>'));
		assertEquals(true, SBytes.isNarrow('?'));
		assertEquals(true, SBytes.isNarrow('@'));
		assertEquals(true, SBytes.isNarrow('A'));
		assertEquals(true, SBytes.isNarrow('B'));
		assertEquals(true, SBytes.isNarrow('C'));
		assertEquals(true, SBytes.isNarrow('D'));
		assertEquals(true, SBytes.isNarrow('E'));
		assertEquals(true, SBytes.isNarrow('F'));
		assertEquals(true, SBytes.isNarrow('G'));
		assertEquals(true, SBytes.isNarrow('H'));
		assertEquals(true, SBytes.isNarrow('I'));
		assertEquals(true, SBytes.isNarrow('J'));
		assertEquals(true, SBytes.isNarrow('K'));
		assertEquals(true, SBytes.isNarrow('L'));
		assertEquals(true, SBytes.isNarrow('M'));
		assertEquals(true, SBytes.isNarrow('N'));
		assertEquals(true, SBytes.isNarrow('O'));
		assertEquals(true, SBytes.isNarrow('P'));
		assertEquals(true, SBytes.isNarrow('Q'));
		assertEquals(true, SBytes.isNarrow('R'));
		assertEquals(true, SBytes.isNarrow('S'));
		assertEquals(true, SBytes.isNarrow('T'));
		assertEquals(true, SBytes.isNarrow('U'));
		assertEquals(true, SBytes.isNarrow('V'));
		assertEquals(true, SBytes.isNarrow('W'));
		assertEquals(true, SBytes.isNarrow('X'));
		assertEquals(true, SBytes.isNarrow('Y'));
		assertEquals(true, SBytes.isNarrow('Z'));
		assertEquals(true, SBytes.isNarrow('['));
		assertEquals(true, SBytes.isNarrow('\\'));
		assertEquals(true, SBytes.isNarrow(']'));
		assertEquals(true, SBytes.isNarrow('^'));
		assertEquals(true, SBytes.isNarrow('_'));
		assertEquals(true, SBytes.isNarrow('`'));
		assertEquals(true, SBytes.isNarrow('a'));
		assertEquals(true, SBytes.isNarrow('b'));
		assertEquals(true, SBytes.isNarrow('c'));
		assertEquals(true, SBytes.isNarrow('d'));
		assertEquals(true, SBytes.isNarrow('e'));
		assertEquals(true, SBytes.isNarrow('f'));
		assertEquals(true, SBytes.isNarrow('g'));
		assertEquals(true, SBytes.isNarrow('h'));
		assertEquals(true, SBytes.isNarrow('i'));
		assertEquals(true, SBytes.isNarrow('j'));
		assertEquals(true, SBytes.isNarrow('k'));
		assertEquals(true, SBytes.isNarrow('l'));
		assertEquals(true, SBytes.isNarrow('m'));
		assertEquals(true, SBytes.isNarrow('n'));
		assertEquals(true, SBytes.isNarrow('o'));
		assertEquals(true, SBytes.isNarrow('p'));
		assertEquals(true, SBytes.isNarrow('q'));
		assertEquals(true, SBytes.isNarrow('r'));
		assertEquals(true, SBytes.isNarrow('s'));
		assertEquals(true, SBytes.isNarrow('t'));
		assertEquals(true, SBytes.isNarrow('u'));
		assertEquals(true, SBytes.isNarrow('v'));
		assertEquals(true, SBytes.isNarrow('w'));
		assertEquals(true, SBytes.isNarrow('x'));
		assertEquals(true, SBytes.isNarrow('y'));
		assertEquals(true, SBytes.isNarrow('z'));
		assertEquals(true, SBytes.isNarrow('{'));
		assertEquals(true, SBytes.isNarrow('|'));
		assertEquals(true, SBytes.isNarrow('}'));
		assertEquals(true, SBytes.isNarrow('~'));
		assertEquals(true, SBytes.isNarrow('¡'));
		assertEquals(true, SBytes.isNarrow('¢'));
		assertEquals(true, SBytes.isNarrow('£'));
		assertEquals(true, SBytes.isNarrow('¤'));
		assertEquals(true, SBytes.isNarrow('¥'));
		assertEquals(true, SBytes.isNarrow('¦'));
		assertEquals(true, SBytes.isNarrow('§'));
		assertEquals(true, SBytes.isNarrow('¨'));
		assertEquals(true, SBytes.isNarrow('©'));
		assertEquals(true, SBytes.isNarrow('ª'));
		assertEquals(true, SBytes.isNarrow('«'));
		assertEquals(true, SBytes.isNarrow('¬'));
		assertEquals(true, SBytes.isNarrow('­'));
		assertEquals(true, SBytes.isNarrow('®'));
		assertEquals(true, SBytes.isNarrow('¯'));
		assertEquals(true, SBytes.isNarrow('°'));
		assertEquals(true, SBytes.isNarrow('±'));
		assertEquals(true, SBytes.isNarrow('²'));
		assertEquals(true, SBytes.isNarrow('³'));
		assertEquals(true, SBytes.isNarrow('´'));
		assertEquals(true, SBytes.isNarrow('µ'));
		assertEquals(true, SBytes.isNarrow('¶'));
		assertEquals(true, SBytes.isNarrow('·'));
		assertEquals(true, SBytes.isNarrow('¸'));
		assertEquals(true, SBytes.isNarrow('¹'));
		assertEquals(true, SBytes.isNarrow('º'));
		assertEquals(true, SBytes.isNarrow('»'));
		assertEquals(true, SBytes.isNarrow('¼'));
		assertEquals(true, SBytes.isNarrow('½'));
		assertEquals(true, SBytes.isNarrow('¾'));
		assertEquals(true, SBytes.isNarrow('¿'));
		assertEquals(true, SBytes.isNarrow('À'));
		assertEquals(true, SBytes.isNarrow('Á'));
		assertEquals(true, SBytes.isNarrow('Â'));
		assertEquals(true, SBytes.isNarrow('Ã'));
		assertEquals(true, SBytes.isNarrow('Ä'));
		assertEquals(true, SBytes.isNarrow('Å'));
		assertEquals(true, SBytes.isNarrow('Æ'));
		assertEquals(true, SBytes.isNarrow('Ç'));
		assertEquals(true, SBytes.isNarrow('È'));
		assertEquals(true, SBytes.isNarrow('É'));
		assertEquals(true, SBytes.isNarrow('Ê'));
		assertEquals(true, SBytes.isNarrow('Ë'));
		assertEquals(true, SBytes.isNarrow('Ì'));
		assertEquals(true, SBytes.isNarrow('Í'));
		assertEquals(true, SBytes.isNarrow('Î'));
		assertEquals(true, SBytes.isNarrow('Ï'));
		assertEquals(true, SBytes.isNarrow('Ð'));
		assertEquals(true, SBytes.isNarrow('Ñ'));
		assertEquals(true, SBytes.isNarrow('Ò'));
		assertEquals(true, SBytes.isNarrow('Ó'));
		assertEquals(true, SBytes.isNarrow('Ô'));
		assertEquals(true, SBytes.isNarrow('Õ'));
		assertEquals(true, SBytes.isNarrow('Ö'));
		assertEquals(true, SBytes.isNarrow('×'));
		assertEquals(true, SBytes.isNarrow('Ø'));
		assertEquals(true, SBytes.isNarrow('Ù'));
		assertEquals(true, SBytes.isNarrow('Ú'));
		assertEquals(true, SBytes.isNarrow('Û'));
		assertEquals(true, SBytes.isNarrow('Ü'));
		assertEquals(true, SBytes.isNarrow('Ý'));
		assertEquals(true, SBytes.isNarrow('Þ'));
		assertEquals(true, SBytes.isNarrow('ß'));
	}

	@Test
	public void testIsSymbol() {
		assertEquals(false, SBytes.isSymbol('‚ '));
		assertEquals(false, SBytes.isSymbol('‚P'));
		assertEquals(false, SBytes.isSymbol('‚`'));
		
		assertEquals(true, SBytes.isSymbol(' '));
		assertEquals(true, SBytes.isSymbol('!'));
		assertEquals(true, SBytes.isSymbol('"'));
		assertEquals(true, SBytes.isSymbol('#'));
		assertEquals(true, SBytes.isSymbol('$'));
		assertEquals(true, SBytes.isSymbol('%'));
		assertEquals(true, SBytes.isSymbol('&'));
		assertEquals(true, SBytes.isSymbol('\''));
		assertEquals(true, SBytes.isSymbol('('));
		assertEquals(true, SBytes.isSymbol(')'));
		assertEquals(true, SBytes.isSymbol('*'));
		assertEquals(true, SBytes.isSymbol('+'));
		assertEquals(true, SBytes.isSymbol(','));
		assertEquals(true, SBytes.isSymbol('-'));
		assertEquals(true, SBytes.isSymbol('.'));
		assertEquals(true, SBytes.isSymbol('/'));
		
		assertEquals(false, SBytes.isSymbol('0'));
		assertEquals(false, SBytes.isSymbol('1'));
		assertEquals(false, SBytes.isSymbol('2'));
		assertEquals(false, SBytes.isSymbol('3'));
		assertEquals(false, SBytes.isSymbol('4'));
		assertEquals(false, SBytes.isSymbol('5'));
		assertEquals(false, SBytes.isSymbol('6'));
		assertEquals(false, SBytes.isSymbol('7'));
		assertEquals(false, SBytes.isSymbol('8'));
		assertEquals(false, SBytes.isSymbol('9'));

		assertEquals(true, SBytes.isSymbol(':'));
		assertEquals(true, SBytes.isSymbol(';'));
		assertEquals(true, SBytes.isSymbol('<'));
		assertEquals(true, SBytes.isSymbol('='));
		assertEquals(true, SBytes.isSymbol('>'));
		assertEquals(true, SBytes.isSymbol('?'));
		assertEquals(true, SBytes.isSymbol('@'));

		assertEquals(false, SBytes.isSymbol('A'));
		assertEquals(false, SBytes.isSymbol('B'));
		assertEquals(false, SBytes.isSymbol('C'));
		assertEquals(false, SBytes.isSymbol('D'));
		assertEquals(false, SBytes.isSymbol('E'));
		assertEquals(false, SBytes.isSymbol('F'));
		assertEquals(false, SBytes.isSymbol('G'));
		assertEquals(false, SBytes.isSymbol('H'));
		assertEquals(false, SBytes.isSymbol('I'));
		assertEquals(false, SBytes.isSymbol('J'));
		assertEquals(false, SBytes.isSymbol('K'));
		assertEquals(false, SBytes.isSymbol('L'));
		assertEquals(false, SBytes.isSymbol('M'));
		assertEquals(false, SBytes.isSymbol('N'));
		assertEquals(false, SBytes.isSymbol('O'));
		assertEquals(false, SBytes.isSymbol('P'));
		assertEquals(false, SBytes.isSymbol('Q'));
		assertEquals(false, SBytes.isSymbol('R'));
		assertEquals(false, SBytes.isSymbol('S'));
		assertEquals(false, SBytes.isSymbol('T'));
		assertEquals(false, SBytes.isSymbol('U'));
		assertEquals(false, SBytes.isSymbol('V'));
		assertEquals(false, SBytes.isSymbol('W'));
		assertEquals(false, SBytes.isSymbol('X'));
		assertEquals(false, SBytes.isSymbol('Y'));
		assertEquals(false, SBytes.isSymbol('Z'));

		assertEquals(true, SBytes.isSymbol('['));
		assertEquals(true, SBytes.isSymbol('\\'));
		assertEquals(true, SBytes.isSymbol(']'));
		assertEquals(true, SBytes.isSymbol('^'));
		assertEquals(true, SBytes.isSymbol('_'));
		assertEquals(true, SBytes.isSymbol('`'));

		assertEquals(false, SBytes.isSymbol('a'));
		assertEquals(false, SBytes.isSymbol('b'));
		assertEquals(false, SBytes.isSymbol('c'));
		assertEquals(false, SBytes.isSymbol('d'));
		assertEquals(false, SBytes.isSymbol('e'));
		assertEquals(false, SBytes.isSymbol('f'));
		assertEquals(false, SBytes.isSymbol('g'));
		assertEquals(false, SBytes.isSymbol('h'));
		assertEquals(false, SBytes.isSymbol('i'));
		assertEquals(false, SBytes.isSymbol('j'));
		assertEquals(false, SBytes.isSymbol('k'));
		assertEquals(false, SBytes.isSymbol('l'));
		assertEquals(false, SBytes.isSymbol('m'));
		assertEquals(false, SBytes.isSymbol('n'));
		assertEquals(false, SBytes.isSymbol('o'));
		assertEquals(false, SBytes.isSymbol('p'));
		assertEquals(false, SBytes.isSymbol('q'));
		assertEquals(false, SBytes.isSymbol('r'));
		assertEquals(false, SBytes.isSymbol('s'));
		assertEquals(false, SBytes.isSymbol('t'));
		assertEquals(false, SBytes.isSymbol('u'));
		assertEquals(false, SBytes.isSymbol('v'));
		assertEquals(false, SBytes.isSymbol('w'));
		assertEquals(false, SBytes.isSymbol('x'));
		assertEquals(false, SBytes.isSymbol('y'));
		assertEquals(false, SBytes.isSymbol('z'));

		assertEquals(true, SBytes.isSymbol('{'));
		assertEquals(true, SBytes.isSymbol('|'));
		assertEquals(true, SBytes.isSymbol('}'));
		assertEquals(true, SBytes.isSymbol('~'));
	}

	@Test
	public void testIsKana() {
		assertEquals(false, SBytes.isKana(' '));
		assertEquals(false, SBytes.isKana('‚ '));
		assertEquals(false, SBytes.isKana('‚P'));
		assertEquals(false, SBytes.isKana('‚`'));
		assertEquals(false, SBytes.isKana('ƒA'));
		assertEquals(false, SBytes.isKana('1'));
		assertEquals(false, SBytes.isKana('A'));
		assertEquals(true, SBytes.isKana('¡'));
		assertEquals(true, SBytes.isKana('¢'));
		assertEquals(true, SBytes.isKana('£'));
		assertEquals(true, SBytes.isKana('¤'));
		assertEquals(true, SBytes.isKana('¥'));
		assertEquals(true, SBytes.isKana('¦'));
		assertEquals(true, SBytes.isKana('§'));
		assertEquals(true, SBytes.isKana('¨'));
		assertEquals(true, SBytes.isKana('©'));
		assertEquals(true, SBytes.isKana('ª'));
		assertEquals(true, SBytes.isKana('«'));
		assertEquals(true, SBytes.isKana('¬'));
		assertEquals(true, SBytes.isKana('­'));
		assertEquals(true, SBytes.isKana('®'));
		assertEquals(true, SBytes.isKana('¯'));
		assertEquals(true, SBytes.isKana('°'));
		assertEquals(true, SBytes.isKana('±'));
		assertEquals(true, SBytes.isKana('²'));
		assertEquals(true, SBytes.isKana('³'));
		assertEquals(true, SBytes.isKana('´'));
		assertEquals(true, SBytes.isKana('µ'));
		assertEquals(true, SBytes.isKana('¶'));
		assertEquals(true, SBytes.isKana('·'));
		assertEquals(true, SBytes.isKana('¸'));
		assertEquals(true, SBytes.isKana('¹'));
		assertEquals(true, SBytes.isKana('º'));
		assertEquals(true, SBytes.isKana('»'));
		assertEquals(true, SBytes.isKana('¼'));
		assertEquals(true, SBytes.isKana('½'));
		assertEquals(true, SBytes.isKana('¾'));
		assertEquals(true, SBytes.isKana('¿'));
		assertEquals(true, SBytes.isKana('À'));
		assertEquals(true, SBytes.isKana('Á'));
		assertEquals(true, SBytes.isKana('Â'));
		assertEquals(true, SBytes.isKana('Ã'));
		assertEquals(true, SBytes.isKana('Ä'));
		assertEquals(true, SBytes.isKana('Å'));
		assertEquals(true, SBytes.isKana('Æ'));
		assertEquals(true, SBytes.isKana('Ç'));
		assertEquals(true, SBytes.isKana('È'));
		assertEquals(true, SBytes.isKana('É'));
		assertEquals(true, SBytes.isKana('Ê'));
		assertEquals(true, SBytes.isKana('Ë'));
		assertEquals(true, SBytes.isKana('Ì'));
		assertEquals(true, SBytes.isKana('Í'));
		assertEquals(true, SBytes.isKana('Î'));
		assertEquals(true, SBytes.isKana('Ï'));
		assertEquals(true, SBytes.isKana('Ð'));
		assertEquals(true, SBytes.isKana('Ñ'));
		assertEquals(true, SBytes.isKana('Ò'));
		assertEquals(true, SBytes.isKana('Ó'));
		assertEquals(true, SBytes.isKana('Ô'));
		assertEquals(true, SBytes.isKana('Õ'));
		assertEquals(true, SBytes.isKana('Ö'));
		assertEquals(true, SBytes.isKana('×'));
		assertEquals(true, SBytes.isKana('Ø'));
		assertEquals(true, SBytes.isKana('Ù'));
		assertEquals(true, SBytes.isKana('Ú'));
		assertEquals(true, SBytes.isKana('Û'));
		assertEquals(true, SBytes.isKana('Ü'));
		assertEquals(true, SBytes.isKana('Ý'));
		assertEquals(true, SBytes.isKana('Þ'));
		assertEquals(true, SBytes.isKana('ß'));
	}

	@Test
	public void testIsDigit() {
		assertEquals(false, SBytes.isDigit(' '));
		assertEquals(false, SBytes.isDigit('‚ '));
		assertEquals(false, SBytes.isDigit('‚P'));
		assertEquals(false, SBytes.isDigit('‚`'));
		assertEquals(false, SBytes.isDigit('ƒA'));
		assertEquals(false, SBytes.isDigit('A'));

		assertEquals(true, SBytes.isDigit('0'));
		assertEquals(true, SBytes.isDigit('1'));
		assertEquals(true, SBytes.isDigit('2'));
		assertEquals(true, SBytes.isDigit('3'));
		assertEquals(true, SBytes.isDigit('4'));
		assertEquals(true, SBytes.isDigit('5'));
		assertEquals(true, SBytes.isDigit('6'));
		assertEquals(true, SBytes.isDigit('7'));
		assertEquals(true, SBytes.isDigit('8'));
		assertEquals(true, SBytes.isDigit('9'));
	}
	@Test
	public void testIsAlphabet() {
		assertEquals(false, SBytes.isAlphabet(' '));
		assertEquals(false, SBytes.isAlphabet('‚ '));
		assertEquals(false, SBytes.isAlphabet('‚P'));
		assertEquals(false, SBytes.isAlphabet('‚`'));
		assertEquals(false, SBytes.isAlphabet('ƒA'));
		assertEquals(false, SBytes.isAlphabet('1'));

		assertEquals(true, SBytes.isAlphabet('A'));
		assertEquals(true, SBytes.isAlphabet('B'));
		assertEquals(true, SBytes.isAlphabet('C'));
		assertEquals(true, SBytes.isAlphabet('D'));
		assertEquals(true, SBytes.isAlphabet('E'));
		assertEquals(true, SBytes.isAlphabet('F'));
		assertEquals(true, SBytes.isAlphabet('G'));
		assertEquals(true, SBytes.isAlphabet('H'));
		assertEquals(true, SBytes.isAlphabet('I'));
		assertEquals(true, SBytes.isAlphabet('J'));
		assertEquals(true, SBytes.isAlphabet('K'));
		assertEquals(true, SBytes.isAlphabet('L'));
		assertEquals(true, SBytes.isAlphabet('M'));
		assertEquals(true, SBytes.isAlphabet('N'));
		assertEquals(true, SBytes.isAlphabet('O'));
		assertEquals(true, SBytes.isAlphabet('P'));
		assertEquals(true, SBytes.isAlphabet('Q'));
		assertEquals(true, SBytes.isAlphabet('R'));
		assertEquals(true, SBytes.isAlphabet('S'));
		assertEquals(true, SBytes.isAlphabet('T'));
		assertEquals(true, SBytes.isAlphabet('U'));
		assertEquals(true, SBytes.isAlphabet('V'));
		assertEquals(true, SBytes.isAlphabet('W'));
		assertEquals(true, SBytes.isAlphabet('X'));
		assertEquals(true, SBytes.isAlphabet('Y'));
		assertEquals(true, SBytes.isAlphabet('Z'));
		assertEquals(true, SBytes.isAlphabet('a'));
		assertEquals(true, SBytes.isAlphabet('b'));
		assertEquals(true, SBytes.isAlphabet('c'));
		assertEquals(true, SBytes.isAlphabet('d'));
		assertEquals(true, SBytes.isAlphabet('e'));
		assertEquals(true, SBytes.isAlphabet('f'));
		assertEquals(true, SBytes.isAlphabet('g'));
		assertEquals(true, SBytes.isAlphabet('h'));
		assertEquals(true, SBytes.isAlphabet('i'));
		assertEquals(true, SBytes.isAlphabet('j'));
		assertEquals(true, SBytes.isAlphabet('k'));
		assertEquals(true, SBytes.isAlphabet('l'));
		assertEquals(true, SBytes.isAlphabet('m'));
		assertEquals(true, SBytes.isAlphabet('n'));
		assertEquals(true, SBytes.isAlphabet('o'));
		assertEquals(true, SBytes.isAlphabet('p'));
		assertEquals(true, SBytes.isAlphabet('q'));
		assertEquals(true, SBytes.isAlphabet('r'));
		assertEquals(true, SBytes.isAlphabet('s'));
		assertEquals(true, SBytes.isAlphabet('t'));
		assertEquals(true, SBytes.isAlphabet('u'));
		assertEquals(true, SBytes.isAlphabet('v'));
		assertEquals(true, SBytes.isAlphabet('w'));
		assertEquals(true, SBytes.isAlphabet('x'));
		assertEquals(true, SBytes.isAlphabet('y'));
		assertEquals(true, SBytes.isAlphabet('z'));
	}


//	enumit(symbolList);
//	public static void enumit(List<String> list) {
//		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
//			String str = (String) iterator.next();
//			System.out.println(str);
//		}
//	}
	public static void createTest(){
		List<String> digitList = SBytes.getDigitList();
		for (Iterator<String> iterator = digitList.iterator(); iterator.hasNext();) {
			String str = iterator.next();
			char val = str.charAt(0);
			boolean ans = SBytes.isDigit(val);
			System.out.println(" assertEquals("+String.valueOf(ans)+", SingleBytes.isDigit(\'"+val+"\'));");
		}

		List<String> alphaList = SBytes.getAlphabetList();
		for (Iterator<String> iterator = alphaList.iterator(); iterator.hasNext();) {
			String str = iterator.next();
			char val = str.charAt(0);
			boolean ans = SBytes.isAlphabet(val);
			System.out.println(" assertEquals("+String.valueOf(ans)+", SingleBytes.isAlphabet(\'"+val+"\'));");
		}

		List<String> kanaList = SBytes.getKanaList();
		for (Iterator<String> iterator = kanaList.iterator(); iterator.hasNext();) {
			String str = iterator.next();
			char val = str.charAt(0);
			boolean ans = SBytes.isKana(val);
			System.out.println(" assertEquals("+String.valueOf(ans)+", SingleBytes.isKana(\'"+val+"\'));");
		}

		List<String> symbolList = SBytes.getSymbolList();
		for (Iterator<String> iterator = symbolList.iterator(); iterator.hasNext();) {
			String str = iterator.next();
			char val = str.charAt(0);
			boolean ans = SBytes.isSymbol(val);
			System.out.println(" assertEquals("+String.valueOf(ans)+", SingleBytes.isSymbol(\'"+val+"\'));");
		}

		symbolList.addAll(kanaList);
		for (Iterator<String> iterator = symbolList.iterator(); iterator.hasNext();) {
			String str = iterator.next();
			char val = str.charAt(0);
			boolean ans = SBytes.isNarrow(val);
			System.out.println(" assertEquals("+String.valueOf(ans)+", SingleBytes.isNarrow(\'"+val+"\'));");
		}
		
	}
	public static void main(String[] argv) {
		createTest();
		// enumDigit();
		// enumAlpha();
		// enumSymbol();
		// enumAN();
		// enumKATAKANA();
	}

}
