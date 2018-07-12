package kyPkg.jobs;
import kyPkg.socks.SMTPmini;
import kyPkg.sql.Inf_JDBC;
import kyPkg.sql.JDBC;
import kyPkg.util.*;
import java.io.*;
import java.util.*;
import java.text.*;
import java.sql.*;
public class Mission01{
	public static final String DEFAULT_EMAIL = "mi-gotoh@tokyu-agc.co.jp";
	//-------------------------------------------------------------------------
	//	�ʂ����Ă��̃N���X�͂��悤����Ă�����̂Ȃ̂��H�P�Ȃ�e�X�g�Ȃ̂��@�@@20100917
	//-------------------------------------------------------------------------
	public static void mission(){
		GregorianCalendar calendar = new GregorianCalendar();
		SimpleDateFormat sf = new SimpleDateFormat("MM��dd��");
		String wMMDD = sf.format(calendar.getTime());
		System.out.println("�����́A"+ wMMDD );
		//---------------------------------------------------------------------
		// DB connection 
		//---------------------------------------------------------------------
		Inf_JDBC ins = new JDBC(
				"jdbc:mysql://agcqbr/test?useUnicode=true&characterEncoding=Windows-31J",
				"root","fatmanri2m"
		);
		String sql = "";
		Connection con = null;
		Statement stmt = null;
		//---------------------------------------------------------------------
		List list = new ArrayList();
		try{
			con  = ins.getConnection();   // con���k���Ȃ�G���[�����I�I
			stmt = con.createStatement(); // �X�e�[�g�����g����
			// stmt���k���Ȃ�G���[�����I�I
			sql  = "SELECT distinct email FROM commisions order by email;";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				System.out.println  (">"+rs.getString("email") );
				list.add( rs.getString("email") );
			}
		}catch(Exception ee){ ee.printStackTrace(); }
		//---------------------------------------------------------------------
		String wUsernm = "";
		String wUserid = "";
		String toEmail  = "";
		String wSrc    = "";
		String wDes    = "";
		List xArrayX1 = new ArrayList();
		List xArrayX2 = new ArrayList();
		List xArrayX3 = new ArrayList();
		try{
			sql = "select * from commisions where email = ?;";
			PreparedStatement pstmt = con.prepareStatement(sql);
			for(int i = 0;i<list.size();i++){
				toEmail = list.get(i).toString();
				System.out.println  ("#wEmail("+i+")=>"+toEmail );
				pstmt.setString(1,toEmail );
				ResultSet rs = pstmt.executeQuery();
				String AllMsg = "";
				while( rs.next() ){
					wUsernm = rs.getString("usernm");
					wUserid = rs.getString("userid");
					wSrc    = rs.getString("condition");
					wDes    = rs.getString("command");
					wSrc    = wSrc.replaceAll("mmdd",wMMDD);
					wDes    = wDes.replaceAll("mmdd",wMMDD);
					System.out.println("��wUsernm =>"+wUsernm);
					System.out.println("��wUserid =>"+wUserid);
					//System.out.println("��wSrc =>"+wSrc);
					//System.out.println("��wDes =>"+wDes);
					if(new File(wSrc).exists()){
						System.out.println("���\�[�X�p�X�͑��݂��܂� =>"+wSrc);
						String wCmd01 = "cmd.exe /c xcopy " + wSrc + " " + wDes + " /e /c /h /I /Y";
						String wCmd02 = "cmd.exe /c rmdir " + wSrc + " /S /Q";
						System.out.println("��"+ wCmd01 );
						System.out.println("��"+ wCmd02 );
						xArrayX1.add( wSrc   );
						xArrayX2.add( wCmd01 );
						xArrayX3.add( wCmd02 );
						if (xArrayX1.size()>0){
					    	String wMsg = new Shell().execute(xArrayX2);
							if(new File(wDes).exists()){
								System.out.println("�����݂��܂� =>"+wDes);
								AllMsg = AllMsg.concat(wMsg);
								System.out.println("�R�s�[���ꂽ�悤�ł� =>"+wDes);
					    	 	String wMsg2 = new Shell().execute(xArrayX3);
								int wIdx = wMsg.indexOf(wMsg2+" �̃t�@�C�����R�s�[���܂���");
								System.out.println("���b�Z�[�W��"+wMsg);
								if ( wIdx >0 ){
									System.out.println("������");
								}else{
									System.out.println("����܂���");
								}
							}else{
								System.out.println("�����݂��܂��� =>"+wDes);
							}
						}

					}else{
						System.out.println("�����݂��܂��� =>"+wSrc);
					}
				}
				if(!AllMsg.equals("")){
					//-------------------------------------------------------------------------------
					// ��������[������
					//-------------------------------------------------------------------------------
					String subject   = "Mission01";
					String[] msgBody = {"a Message To You"};
					msgBody[0] = AllMsg;
					String mailFrm = DEFAULT_EMAIL; // ���M�惁�[���A�h���X�i�Վ��j
					new SMTPmini(mailFrm,toEmail,subject,Arrays.asList(msgBody));
				}
			}
		}catch(Exception e){ e.printStackTrace(); }
		//-------------------------------------------------------------
		// DB disconnection 
		//-------------------------------------------------------------
		try{
			ins.freeConnection(con);
		}catch(Exception ee){ ee.printStackTrace(); }
		//jTa2.setText(wMsg);
	}
}
