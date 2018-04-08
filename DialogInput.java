//
//	■入力ダイアログ
//	
//	●swing 使わない
//	●親ダイアログ、親フレームでも可能に
//		文字列判別イマイチ
//	
//	javac DialogInput.java
//	java DialogInput
//	

//public class DialogInput extends java.awt.Dialog implements java.awt.event.ActionListener
public class DialogInput extends java.awt.Dialog
{
	private java.awt.TextField textField = null;
	
	// コンストラクタ
	DialogInput(
		java.awt.Frame frame,
		String strTitle,
		String strMessage,
		String strValue )
	{
		super( frame, strTitle, true );
		DialogInput( strMessage, strValue );
	}
	
	// コンストラクタ
	DialogInput(
		java.awt.Dialog dialog,
		String strTitle,
		String strMessage,
		String strValue )
	{
		super( dialog, strTitle, true );
		DialogInput( strMessage, strValue );
	}
	
	private void DialogInput( String strMessage, String strValue )
	{
		addWindowListener(
			new java.awt.event.WindowAdapter(){
				public void windowClosing( java.awt.event.WindowEvent e ){
					textField.setText( null );
					dispose();
				}
			}
		);
		
		java.awt.Panel panelNORTH = new java.awt.Panel( new java.awt.BorderLayout() );
		panelNORTH.add( new java.awt.Label( strMessage ), java.awt.BorderLayout.CENTER );
		
		java.awt.Panel panelCENTER = new java.awt.Panel( new java.awt.BorderLayout() );
		textField = new java.awt.TextField( strValue, 30 );
		textField.setFont( new java.awt.Font("MS UI Gothic", java.awt.Font.PLAIN, 14) );
		panelCENTER.add( textField, java.awt.BorderLayout.CENTER );
		
		java.awt.Panel panelSOUTH = new java.awt.Panel( new java.awt.FlowLayout() );
		java.awt.Button buttonHozon = new java.awt.Button( "保存" );
		java.awt.Button buttonCancel= new java.awt.Button( "キャンセル" );
		panelSOUTH.add( buttonHozon );
		panelSOUTH.add( buttonCancel );
		
		buttonHozon.addActionListener(
			new java.awt.event.ActionListener(){
				public void actionPerformed( java.awt.event.ActionEvent event ){
					dispose();
				}
			}
		);
		
		buttonCancel.addActionListener(
			new java.awt.event.ActionListener(){
			public void actionPerformed( java.awt.event.ActionEvent event ){
				textField.setText( null );
				dispose();
		 	}
		});
		
		add( panelNORTH,  java.awt.BorderLayout.NORTH );
		add( panelCENTER, java.awt.BorderLayout.CENTER );
		add( panelSOUTH,  java.awt.BorderLayout.SOUTH );
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
	
	protected String getValue()
	{
		return textField.getText();
	}
	
	public static void main( String args[] )
	{
		String str = new DialogInput( new java.awt.Frame( "main" ),
			"ライセンス認証",
			"ライセンスキーを入力すると正規版になります", "" ).getValue();
		System.err.println( str );
		System.exit( 0 );
		
/*		final java.awt.Frame frame = new java.awt.Frame( "main" );
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
					new DialogInput( frame );
			 	}
			}
		);
		// 
		frame.setLayout( new java.awt.FlowLayout() );
		frame.add( button );
		frame.pack();
		frame.show();
*/
	}
}
