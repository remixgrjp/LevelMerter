//
//	■ダイアログ
//	
//	●swing 使わない
//	●メッセージの複数行ラベル表示
//	●親ダイアログ、親フレームでも可能に
//	●ラベル表示のメッセージをEOFで
//	
//	javac DialogCustom.java
//	java DialogCustom
//	
public class DialogCustom extends java.awt.Dialog implements java.awt.event.ActionListener
{
	final static String EOL = System.getProperty( "line.separator" );
	
	public void actionPerformed( java.awt.event.ActionEvent actionEvent )
	{
		if( actionEvent.getActionCommand().equals( "OK" ) )
			dispose();
	}
	
	// コンストラクタ
	DialogCustom( java.awt.Frame frame, String strMessage )
	{
		super( frame, frame.getTitle(), true );
		DialogCustom( strMessage );
	}
	
	// コンストラクタ
	DialogCustom( java.awt.Dialog dialog, String strMessage )
	{
		super( dialog, dialog.getTitle(), true );
		DialogCustom( strMessage );
	}
	
	protected void DialogCustom( String strMessage )
	{
		addWindowListener(
			new java.awt.event.WindowAdapter(){
				public void windowClosing( java.awt.event.WindowEvent e ){
					dispose();
				}
			}
		);
		
		// ラベルを行数分だけグリッドレイアウトで追加
	//	String strArray[] = strMessage.split( "^" ); // 効かない
		String strArray[] = strMessage.split( EOL );
	//	String strArray[] = strMessage.replaceAll( ":", EOL ).split( EOL );
		java.awt.Label[] label = new java.awt.Label[strArray.length];
		java.awt.Panel panelMessage =
			new java.awt.Panel( new java.awt.GridLayout( strArray.length, 1 ) );
		for( int i = 0; i < strArray.length; panelMessage.add( label[i++] ) )
			label[i] = new java.awt.Label( strArray[i], java.awt.Label.LEFT );
		
		// ボタンをフローレイアウトで追加
		java.awt.Panel panelButton = new java.awt.Panel( new java.awt.FlowLayout() );
		java.awt.Button buttonOK = new java.awt.Button( "OK" );
		buttonOK.addActionListener( this );
		panelButton.add( buttonOK );
		
		add( panelButton,  java.awt.BorderLayout.SOUTH );
		add( panelMessage, java.awt.BorderLayout.NORTH );
		pack();
		// スクリーン中央 pack と show の間でないと反映されない
		java.awt.Dimension dimensionScreen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		java.awt.Dimension dimensionSelf = getPreferredSize();
		setLocation(
			( dimensionScreen.width - dimensionSelf.width ) / 2
		,	( dimensionScreen.height- dimensionSelf.height) / 2
		);
		show();
	}
	
	public static void main( String args[] )
	{
		final java.awt.Frame frame = new java.awt.Frame( "main" );
		frame.addWindowListener(
			new java.awt.event.WindowAdapter(){
				public void windowClosing( java.awt.event.WindowEvent e ){
					System.exit( 0 );
				}
			}
		);
		// 
		java.awt.Button button = new java.awt.Button( "test" );
		button.addActionListener(
			new java.awt.event.ActionListener()
			{
				public void actionPerformed( java.awt.event.ActionEvent event )
				{
					new DialogCustom( frame, "エラー発生" + EOL + "エラー発生" + EOL + "エラー発生" );
			 	}
			}
		);
		// 
		frame.setLayout( new java.awt.FlowLayout() );
		frame.add( button );
		frame.pack();
		// スクリーン中央 pack と show の間でないと反映されない
		java.awt.Dimension dimensionScreen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		java.awt.Dimension dimensionSelf = frame.getPreferredSize();
		frame.setLocation(
			( dimensionScreen.width - dimensionSelf.width ) / 2
		,	( dimensionScreen.height- dimensionSelf.height) / 2
		);
		frame.show();

try
{
	new java.io.FileInputStream( "ないファイル" );
}
catch( Exception exception )
{
	exception.printStackTrace();
	new DialogCustom( frame, exception.toString() );
}


	}
}
