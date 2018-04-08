//	
//	■５．６カスタムコンポーネントの定義
//		javasample/examplesOReilly/section5/MultiLineLabel.java
//		http://www.ora.com/catalog/books/javanut/
//		javaクイックリファレンス
//	
//	●J2SE で推奨されるメソッドへ変更済み
//	●Canvas + image 版
//	●ピーク追加版
//	●image → jarファイル
//	●ちらつき防止オフスクリーン対応
//		graphicsOff の消滅タイミング
//	
//	javac LevelMerter.java
//	java LevelMerter
//	jar cvfm LevelMerter.jar LevelMerter.txt *.class image\*.gif
//

public class LevelMerter
	extends java.awt.Canvas
{
//	java.awt.Toolkit toolkit = getToolkit();
	java.awt.Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
	java.awt.Image[] imagesPeek = {
		toolkit.getImage( this.getClass().getResource( "image/peek0.gif" ) ),
		toolkit.getImage( this.getClass().getResource( "image/peek1.gif" ) ),
		toolkit.getImage( this.getClass().getResource( "image/peek2.gif" ) ),
		toolkit.getImage( this.getClass().getResource( "image/peek3.gif" ) ),
		toolkit.getImage( this.getClass().getResource( "image/peek4.gif" ) ),
		toolkit.getImage( this.getClass().getResource( "image/peek5.gif" ) ),
		toolkit.getImage( this.getClass().getResource( "image/peek6.gif" ) ),
		toolkit.getImage( this.getClass().getResource( "image/peek7.gif" ) ),
		toolkit.getImage( this.getClass().getResource( "image/peek8.gif" ) ),
		toolkit.getImage( this.getClass().getResource( "image/peek9.gif" ) ),
		toolkit.getImage( this.getClass().getResource( "image/peekA.gif" ) )
	};
	java.awt.Image[] imagesLevel = {
		toolkit.getImage( this.getClass().getResource( "image/level0.gif" ) ),
		toolkit.getImage( this.getClass().getResource( "image/level1.gif" ) ),
		toolkit.getImage( this.getClass().getResource( "image/level2.gif" ) ),
		toolkit.getImage( this.getClass().getResource( "image/level3.gif" ) ),
		toolkit.getImage( this.getClass().getResource( "image/level4.gif" ) ),
		toolkit.getImage( this.getClass().getResource( "image/level5.gif" ) ),
		toolkit.getImage( this.getClass().getResource( "image/level6.gif" ) ),
		toolkit.getImage( this.getClass().getResource( "image/level7.gif" ) ),
		toolkit.getImage( this.getClass().getResource( "image/level8.gif" ) ),
		toolkit.getImage( this.getClass().getResource( "image/level9.gif" ) ),
		toolkit.getImage( this.getClass().getResource( "image/levelA.gif" ) )
	};
	java.awt.Image image = imagesLevel[0];	//	簡単のため基準に
	
	//	オフスクリーン用イメージ
	java.awt.Image imageOff;
	//	オフスクリーン用グラフィック
	java.awt.Graphics graphicsOff;
	
	int iValue;
	private int iPeek;
	private int iHold;
	protected int iDelay;
	
	// コンストラクタ
	public LevelMerter()
	{	//	初期値
		iDelay = 50;
		iHold = iDelay;
		iValue = 0;
		iPeek = iValue;
		
		//	イメージ読込完了までブロッキング
		java.awt.MediaTracker mediaTracker =
			new java.awt.MediaTracker( this );
		for( int i=imagesLevel.length-1; 0<=i; i-- )
			mediaTracker.addImage( imagesLevel[i], i );
		try{
			mediaTracker.waitForAll();
		}catch( Exception exception ){
			exception.printStackTrace( System.err );
		};
	}
	
	// Canvas 生成 → コレ → 表示
	public void addNotify()
	{//	スーパークラス addNotify() → フォントメトリックス → サイズ調査
		super.addNotify();
		measure();
		
		//	オフスクリーン用
		imageOff =
			createImage( image.getWidth( this ) , image.getHeight( this ) );
		graphicsOff =
			imageOff.getGraphics();
		//	コンストラクタでは取得に間に合わない
	}
	
	//	サイズ調査	使っていない
	protected void measure(){
		java.awt.FontMetrics fontMetrics = this.getFontMetrics( this.getFont() );
		// If we don't have font metrics yet, just return.
		if( null == fontMetrics )
			return;
//		System.err.println( "★" );
	}
	
	// レイアウトマネージャが最小サイズを取得しに来る
	public java.awt.Dimension getMinimumSize(){
		return new java.awt.Dimension( image.getWidth( this ), image.getHeight( this ) );
	}
	
	// レイアウトマネージャが適切なサイズを取得しに来る
	public java.awt.Dimension getPreferredSize(){
		return new java.awt.Dimension( image.getWidth( this ), image.getHeight( this ) );
	}
	
	public void paint( java.awt.Graphics graphics ){
	///	System.err.println( iValue );
	///	graphics.drawImage( image, 0, 0, this );
		//	描画位置センタリング
		java.awt.Dimension dimension = this.getSize();
	///	System.err.println( dimension.width + "w/h" + dimension.height );
		//	オフスクリーン描画
		graphicsOff.clearRect(
			dimension.width <= image.getWidth( this ) ?
				0 : ( dimension.width - image.getWidth( this ) )/2,
			dimension.height<= image.getHeight( this ) ?
				0 : ( dimension.height- image.getHeight( this ) )/2,
			image.getWidth( this ),
			image.getHeight( this ) );
		graphicsOff.drawImage(
			imagesLevel[iValue],
			dimension.width <= imagesLevel[iValue].getWidth( this ) ?
				0 : ( dimension.width - imagesLevel[iValue].getWidth( this ) )/2,
			dimension.height<= imagesLevel[iValue].getHeight( this ) ?
				0 : ( dimension.height- imagesLevel[iValue].getHeight( this ) )/2,
			imagesLevel[iValue].getWidth( this ),
			imagesLevel[iValue].getHeight( this ),
			this );
		graphicsOff.drawImage(
			imagesPeek[iPeek],
			dimension.width <= imagesPeek[iValue].getWidth( this ) ?
				0 : ( dimension.width - imagesPeek[iPeek].getWidth( this ) )/2,
			dimension.height<= imagesPeek[iValue].getHeight( this ) ?
				0 : ( dimension.height- imagesPeek[iPeek].getHeight( this ) )/2,
			imagesPeek[iPeek].getWidth( this ),
			imagesPeek[iPeek].getHeight( this ),
			this );
		//	スクリーン描画
		graphics.drawImage(
			imageOff,
			dimension.width <= image.getWidth( this ) ?
				0 : ( dimension.width - image.getWidth( this ) )/2,
			dimension.height<= image.getHeight( this ) ?
				0 : ( dimension.height- image.getHeight( this ) )/2,
			image.getWidth( this ),
			image.getHeight( this ),
			this );
		//	ここでは適当
		if(  ( 0 > iHold-- ) || ( iPeek < iValue ) ){
			iPeek = iValue;
			iHold = iDelay;
		}
	}
	
	public static void main( String args[] )
	{
		final LevelMerter canvas = new LevelMerter();
		java.awt.Frame frame = new java.awt.Frame( "テスト" );
		//	クローズ処理
		frame.addWindowListener(  new java.awt.event.WindowAdapter(){
			public void windowClosing( java.awt.event.WindowEvent we ){
canvas.graphicsOff = null;
				System.exit( 0 );
			}
		} );
		frame.add( canvas );
		frame.pack();
		frame.show();
		
	//	canvas.setBackground( java.awt.Color.BLACK );
	//	canvas.iValue = 10;
	//	canvas.repaint();
//		controlLevelMerter.start();

		for( int i=200; 0<=i; i-- )
			try{
				Thread.sleep( 50 );
				canvas.iValue = (int)( Math.random() * canvas.imagesLevel.length );
				canvas.repaint();
			}catch( Exception exception ){
				exception.printStackTrace( System.err );
			}

	}
}
