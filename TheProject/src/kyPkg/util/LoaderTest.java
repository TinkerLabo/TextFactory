package kyPkg.util;
import java.lang.reflect.*;
public class LoaderTest {
	public LoaderTest() {
	}
	public static void classInfo(String args) {
		try {
			Class cls = Class.forName(args);
			Package pkg = cls.getPackage();
			Class sup =	cls.getSuperclass(); 			// SuperClass
			Constructor[] cns = cls.getConstructors(); 	// Constructors
			Method[] methods =cls.getDeclaredMethods(); // Methods
			Field[] fld =cls.getFields();				// Fields
			System.out.println("Name    :"+cls.getName());
			if (sup!=null)
				System.out.println("extends :"+sup.getName());
			if (pkg!=null)
				System.out.println("Package :"+pkg.getName());
			System.out.println( "Fiels:" );
			for( int i=0; i<fld.length; i++ ) {
				System.out.println( "\t"+ getModifiedName( fld[i] ) );
			}
			System.out.println( "Constructor:" );
			for( int i=0; i<cns.length; i++ ) {
				System.out.println( "\t"+ getModifiedName( cns[i] ) );
			}
			System.out.println( "Methods:" );
			for( int i=0; i<methods.length; i++ ) {
				System.out.println( "\t"+ getModifiedName( methods[i] ) );
			}

		} catch (SecurityException e) {
			System.out.println("Can not access the class");
			System.exit(0);
		} catch (ClassNotFoundException e) {
			System.out.println("No such class");
			System.exit(0);
		}
	}
	public static String getModifiedName( Member member ) {
		int modifiers = member.getModifiers();  // CüŽq‚Ìî•ñ
		StringBuffer value = new StringBuffer("");
		if( Modifier.isPublic( modifiers ) ) value.append("public ");
		if( Modifier.isProtected( modifiers ) ) value.append("protected ");
		if( Modifier.isPrivate( modifiers ) ) value.append("private ");
		if( Modifier.isAbstract( modifiers ) ) value.append("abstract ");
		if( Modifier.isNative( modifiers ) ) value.append("native ");
		if( Modifier.isSynchronized( modifiers ) ) value.append("synchronized ");
		if( Modifier.isTransient( modifiers ) ) value.append("transient ");
		if( Modifier.isVolatile( modifiers ) ) value.append("volatile ");
		if( Modifier.isStatic( modifiers ) ) value.append("static ");
		if( Modifier.isFinal( modifiers ) ) value.append("final ");
		if( member instanceof Field ) {
			Class cls = ((Field)member).getType();  // ƒƒ“ƒo‚ÌŒ^
			value.append( cls.getName() );
			value.append( " " );
			value.append( member.getName() );
			return value.toString();
		}
		Class[] prms;
		if( member instanceof Method ) {
			Class cls = ((Method)member).getReturnType();  // ƒƒ\ƒbƒh‚Ì•Ô’l‚ÌŒ^
			prms = ((Method)member).getParameterTypes();
			value.append( cls.getName() );
			value.append( " " );
		}
		else {
			prms = ((Constructor)member).getParameterTypes();
		}
		value.append(member.getName());
		value.append( "(" );
		StringBuffer args = new StringBuffer("");  // ˆø”‚ÌŒ^
		for( int j=0; j<prms.length; j++ ) {
			args.append( prms[j].getName() );
			if( j != prms.length-1 ) args.append( ", " );
		}
		value.append( args.toString() );
		value.append( ")" );
		return value.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LoaderTest.classInfo("kyPkg.util.Archiver");
	}

}
