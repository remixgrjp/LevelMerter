//	
//	■５．６カスタムコンポーネントの定義
//		javasample/examplesOReilly/section5/MultiLineLabel.java
//		http://www.ora.com/catalog/books/javanut/
//		javaクイックリファレンス
//	
//	●J2SE で推奨されるメソッドへ変更済み
//	●Canvas + image 版
//	●サンプルソースを 32728 → 32768 (2^15) に修正
//		おおもとのサンプルは log(10) を ln(10) で近似しているよう
//		METERは計算値から46メモリにしているよう
//		10 / Math.log(10) == 20 / Math.log(10) / 2
//		Math.log( x ) でもあまり変わらない
//	
//	javac ControlLevelMerter.java
//	java ControlLevelMerter
//	jar cvfm LevelMerterDEMO.jar manifestname.txt *.class image\*.gif
//

public class ControlLevelMerter
	extends Thread
{
	static private String strLicence = "THANKYOU";
	static private boolean isTrial = true;
	
	LevelMerter canvas;
	javax.sound.sampled.TargetDataLine targetDataLine = null;
	
	// コンストラクタ
	public ControlLevelMerter( LevelMerter canvas )
	{
		this.canvas = canvas;
	}
	
	public void run()
	{
		long lLimit = System.currentTimeMillis();
		
		//	１サンプル１６ビット＝２バイト
		//	５１２バイトは２５６サンプルで256[回]/16000[回/s]＝16[ms]
		byte[] buffer = new byte[512];
		int length, value, maxValue;
	//	int iBUNKAI = 32768 / (this.canvas.imagesLevel.length-1);
		int iBUNKAI = (32768 + 3276) / (this.canvas.imagesLevel.length-1);//3604
		try{
			javax.sound.sampled.AudioFormat format =
			//	new javax.sound.sampled.AudioFormat( 16000f, 16, 1, true, true );
				new javax.sound.sampled.AudioFormat( 32000f, 16, 2, true, true );
			///	new javax.sound.sampled.AudioFormat( 44100f, 16, 1, true, true );
			///	new javax.sound.sampled.AudioFormat( 48000f, 16, 2, true, true );
			//	TargetDataLineの取得
			javax.sound.sampled.Line.Info info =
				new javax.sound.sampled.DataLine.Info( javax.sound.sampled.TargetDataLine.class, format );
			targetDataLine =
				(javax.sound.sampled.TargetDataLine)javax.sound.sampled.AudioSystem.getLine(info);
			//	TargetDataLineを開く/開始
			targetDataLine.open( format, buffer.length );
			targetDataLine.start();
			//	
			while( (length = targetDataLine.read(buffer, 0, buffer.length) ) > 0 ){
				maxValue = 0;
				for( int i = 0; i < length; i += 2 ){
					value = ((buffer[i] & 0xff) << 8) | (buffer[i + 1] & 0xff);
					if( 32768 < value )	value -= 65536;
				//	maxValue = value > maxValue ? value : maxValue;	// 最適化
					if( maxValue < value  )
						maxValue = value;
				}
			//	this.canvas.iValue = ( maxValue - 8 ) / iBUNKAI;
				if( 0 == maxValue )
					this.canvas.iValue = 0;
				else
				///	this.canvas.iValue = ( maxValue + 3276 ) / iBUNKAI;
					this.canvas.iValue = ( maxValue + 3272 ) / iBUNKAI;
				this.canvas.repaint();
				
				if( isTrial )
					if( System.currentTimeMillis() > lLimit + 5000 )
						break;
			}
		}catch( Exception exception ){
			exception.printStackTrace( System.err );
		}finally{
			// TargetDataLineを閉じる
			targetDataLine.close();
		}
	}
	
	public void ending()
	{
		try{
			//	モニタ終了
			targetDataLine.stop();
			join();
		}catch( Exception exception ){
			exception.printStackTrace( System.err );
		}finally{
			//	TargetDataLineを閉じる
			if( null != targetDataLine )
				targetDataLine.close();
			System.exit( 0 );
		}
	}
	
	public static void main( String args[] )
	{
		String strTitle = "レベルメーター Ver1.0";
		java.awt.Frame frame = new java.awt.Frame( strTitle );
		try{
			java.util.Properties properties = new java.util.Properties();
			java .io.File file = new java .io.File( "LevelMerter.ini" );
			if( file.exists() )
				properties.load( new java.io.FileInputStream( file ) );
			if( strLicence.equals( properties.getProperty( "Licence", "" ) ) )
				isTrial = false;
			else{
				String strPass = new DialogInput( frame,
					"ライセンス認証",
					"ライセンスキーを入力すると正規版になります", "" ).getValue();
				if( strLicence.equals( strPass ) ){
					isTrial = false;
					properties.setProperty( "Licence", strPass );
					properties.store( new java.io.FileOutputStream( file ),
						"LevelMerter Setup Infomation" );
					new DialogCustom( frame, "ライセンス認証に成功しました" );
				}
			}
		}catch( Exception exception ){
			new DialogCustom( frame,
				"エラーが発生しました\r\n" + exception.getMessage() );
			exception.printStackTrace( System.err );
			System.exit( 0 );
		}
		
		final ControlLevelMerter controlLevelMerter =
			new ControlLevelMerter( new LevelMerter() );
		frame = new java.awt.Frame( strTitle );
		//	クローズ処理
		frame.addWindowListener(  new java.awt.event.WindowAdapter(){
			public void windowClosing( java.awt.event.WindowEvent we ){
				controlLevelMerter.ending();
				System.exit( 0 );
			}
		} );
		frame.add( controlLevelMerter.canvas );
		frame.pack();
		frame.setResizable( false );
		frame.show();

		try{
			//	モニタ開始
			controlLevelMerter.start();
			controlLevelMerter.join();
		}catch( Exception exception ){
			new DialogCustom( frame,
				"エラーが発生しました\r\n" + exception.getMessage() );
			exception.printStackTrace( System.err );
			controlLevelMerter.ending();
			System.exit( 0 );
		}
		System.exit( 0 );
	}
}
