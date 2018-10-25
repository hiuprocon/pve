キュメントを書くかわりに簡単なサンプルプログラムを
たくさん書く作戦。

以下の全てのサンプルプログラムには，Testクラスが
入っていて，コンパイルしたら「`java Test`」で実行できる。

ほぼどのサンプルもマウスの左ドラッグで首振り，右の上下の
ドラッグでズームイン，ズームアウト，真ん中ドラッグで，
視界の平行移動ができます。

プログラム中の"x-res:///"で初まる文字列は，外部のデータ
ファイルを読み込む物で，ここで好きなファイルを指定すれば
好きなデータを読み込ませることができる。デフォルトでは
pve-res.jarに入っているファイルを読み込ませるように
書いてある。

### 基本のサンプル

* [TestBase.java](./TestBase.java)
    + タイトル：ベースのプログラム
    + 説明：ProconVEの動作を確認できる最小のプログラム。
      以下のサンプルプログラムは，このプログラムに
      機能を付け足す感じで作られてます。
* [TestManyObj.java](./TestManyObj.java)
    + タイトル：たくさんのオブジェクトを作る
    + 説明：たくさんのオブジェクトを作って
      落してみます。
* [TestPrimitives.java](./TestPrimitives.java)
    + タイトル：基本の形状
    + 説明：基本となる形のオブジェクトを生成してみます。
      (Box,Cone,Cylinder,Sphere,Slope,Corner)
* [TestSize.java](./TestSize.java)
    + タイトル：基本形状の変形
    + 説明：基本形状の各種サイズを指定して
      いろいろな形を作ってみます。
* [TestAppearance.java](./TestAppearance.java)
    + タイトル：オブジェクトの見た目を変える
    + 説明：オブジェクトの見た目をVRML2.0または
      Acerola3Dフォーマットの3DCGデータで置き換えます。
* [TestFreeObj.java](./TestFreeObj.java)
    + タイトル：自由な形を作る
    + 説明：VRML2.0またはAcerola3Dフォーマットの
      3DCGデータを読み込み，3Dオブジェクトとして
      生成します。
* [TestBackground.java](./TestBackground.java)
    + タイトル：背景を設定する
    + 説明：VRML2.0またはAcerola3Dフォーマットの
      3DCGデータを読み込み，背景オブジェクトを
      生成します。
* [TestCamera.java](./TestCamera.java)
    + タイトル：カメラの操作
    + 説明：3DCGにおけるカメラの操作のサンプル。
      この機能はProconVEの機能ではなく，ProconVEが
      利用しているAcerola3Dの機能なので，詳細は
      Acerola3DのAPIなどを参照して下さい。
* [TestWindow.java](./TestWindow.java)
    + タイトル：ウィンドウの大きさを調整する
    + 説明：A3Windowというクラスがウィンドウを
      表しますが，これはjavax.swing.JFrameの
      サブクラスなので，その機能で大きさなど
      操作できます。
* [TestCar.java](./TestCar.java)
    + タイトル：車を走らせる
    + 説明：デフォルトで入っているSimpleCarObjを
      利用して車を生成して走らせるプログラムです。
* [TestMove.java](./TestMove.java)
    + タイトル：物体を速度指定で移動させる
    + 説明：物体の速度を設定することで物体を
      物理法則に従って移動させます。
* [TestMove2.java](./TestMove2.java)
    + タイトル：物体に力を加えて移動させる
    + 説明：物体に力を加えて
      物理法則に従って移動させます。
* [TestMove3.java](./TestMove3.java)
    + タイトル：物体から見た速度で動かす
    + 説明：物体のローカル座標で表された
      速度を使って，物理法則に従って移動させます。
* [TestTurn.java](./TestTurn.java)
    + タイトル：物体を角速度指定で回転させる
    + 説明：物体のに角速度を設定することで
      物体を物理法則に従って回転させます。
* [TestTurn2.java](./TestTurn2.java)
    + タイトル：物体にトルクを与えて回転させる
    + 説明：物体のに角加速度(トルク)を与えて，
      物体を物理法則に従って回転させます。
* [TestTurn3.java](./TestTurn3.java)
    + タイトル：物体から見た角速度で回転させる。
    + 説明：物体のローカル座標で表わされた
      角速度を与えて，物体を物理法則に従って
      回転させます。
* [TestGoTurnRoll.java](./TestGoTurnRoll.java)
    + タイトル：物体に命令を出す感じで動かす。
    + 説明：物体に前に進めとか右に回転しろ
      という指示を出す感じで動かす。
      物理法則に従って移動させます。
* [TestKinematic.java](./TestKinematic.java)
    + タイトル：物理法則を無視して動かす
    + 説明：物理エンジンではなく自前の
      プログラムで動きを完全にコントロールします。
* [TestKeyboardControl.java](./TestKeyboardControl.java)
    + タイトル：キーボードで操作
    + 説明：キーボードで物体を操作できる
      ようにします。
* [TestCollision.java](./TestCollision.java)
    + タイトル：当たり判定をする
    + 説明：物体どうしの当たり判定をします。
* [TestBGM.java](./TestBGM.java)
    + タイトル：BGMを流す
    + 説明：Acerola3Dの機能を利用してBGMを
      流します。
* [TestSoundEffect.java](./TestSoundEffect.java)
    + タイトル：効果音を出す
    + 説明：Acerola3Dの機能を利用して効果音を
      出します。
* [TestMousePicking.java](./TestMousePicking.java)
    + タイトル：物体のクリックを検知
    + 説明：物体をクリックしたことを検知する
      サンプルです。
* [TestMouseMove1.java](./TestMouseMove1.java)
    + タイトル：マウスで物体を動かす1
    + 説明：マウスで物体を動かす方法の
      サンプルプログラムです。こちらは
      物理法則を無視した場合のプログラム
      です。
* [TestMouseMove2.java](./TestMouseMove2.java)
    + タイトル：マウスで物体を動かす2
    + 説明：マウスで物体を動かす方法の
      サンプルプログラムです。こちらは
      物理法則を満した状態で動かす
      プログラムです。
* [TestAvatar.java](./TestAvatar.java)
    + タイトル：アバターの生成
    + 説明：ゲームキャラクターなどを
      簡易に表現するためのアバターの
      使い方のサンプルです。
* [TestChase.java](./TestChase.java)
    + タイトル：カメラ追従
    + 説明：ゲームキャラクターなどを
      カメラが自動で追従するようにする
      方法のサンプルです。
* [TestETRobo.java](./TestETRobo.java)
    + タイトル：ロボットシミュレータ
    + 説明：ETロボコンのシミュレータです。
* [TestRestitution.java](./TestRestitution.java)
    + タイトル：反発係数の設定
    + 説明：物体の衝突の時に，どのくらい
      跳ね返るかを決める反発係数を設定
      するサンプルです．
* [TestFriction.java](./TestFriction.java)
    + タイトル：摩擦係数の設定
    + 説明：物体の摩擦の大きさを決める
      摩擦係数の設定をするサンプルです．
* [TestScene.java](./TestScene.java)
    + タイトル：シーンを利用する
    + 説明：裏の3D仮想空間を作成して，そこに
      メインの3D仮想空間とば別の物を配置して，
      ゲームスタート画面や，終了画面を作ります．
      この機能はProconVEの機能ではなく，ProconVEが
      利用しているAcerola3Dの機能なので，詳細は
      Acerola3DのAPIなどを参照して下さい。
* [TestPaint2D.java](./TestPaint2D.java)
    + タイトル：2Dの描画
    + 説明：ゲームのスコアなど3Dではなく2Dで
      普通に表示したい場合のサンプルプログラム
      です．
* [TestStatic.java](./TestStatic.java)
    + タイトル：STATICオブジェクトのテストプログラム
    + 説明：絶対に動くことのないオブジェクトの
      作成方法についてのサンプルです。
* [TestGhost.java](./TestGhost.java)
    + タイトル：GHOSTオブジェクトのテストプログラム
    + 説明：擦り抜けるけど，当り判定がある
      GHOSTオブジェクトの作成方法についてのサンプル
      です。透明にして通過チェックポイントなどに
      使えます。
* [Test2Player.java](./Test2Player.java)
    + タイトル：2画面で表示する例
    + 説明：一つのウィンドウを2つに分割して
      Player1の視点とPlayer2の視点を表示する例。

### オリジナルなPVEObjectの作成

* [TestHinge.java](./TestHinge.java)
    + タイトル：ヒンジの使い方
    + 説明：2つの物体をヒンジ(蝶番(ちょうつがい))
      で結合して動かすサンプルです。
* [TestHinge2.java](./TestHinge2.java)
    + タイトル：ヒンジの使い方2
    + 説明：ヒンジ(蝶番(ちょうつがい))
      で車輪のようなものを作ります。
* [TestSlider.java](./TestSlider.java)
    + タイトル：スライダーの使い方
    + 説明：2つの物体をスライダーで結合して
      動かすサンプルです。
* [TestPoint2Point.java](./TestPoint2Point.java)
    + タイトル：Point2Pointの使い方
    + 説明：2つの物体をPoint2Pointで結合
      するサンプルです。
* [TestFix.java](./TestFix.java)
    + タイトル：Fixコンストレイント
    + 説明：Fixコンストレイントを使って
      2つの物体を結合して固定する方法の
      サンプルです。
* [TestCompound.java](./TestCompound.java)
    + タイトル：独自オブジェクトの生成
    + 説明：複数のパーツを組合せて
      一つのオブジェクトを作ります。
* [TestRadar.java](./TestRadar.java)
    + タイトル：Raderパーツの使いかた
    + 説明：Raderパーツを他のパーツに
      付けることで，超音波センサーのように
      距離を計ることができます。
* [TestCameraPart.java](./TestCameraPart.java)
    + タイトル：カメラパーツの使い方
    + 説明：オブジェクトにカメラパーツを
      付けてそのオブジェクトからの視界を
      イメージとして得る方法です。
      ロボットの頭や車に付けたりして使います。

### 高度なタスク処理

* [TestStepForward.java](./TestStepForward.java)
    + タイトル：自分で時間をコントロールする
    + 説明：仮想環境の描画とシミュレーションの
      タイミングを自分でコントロールする時の
      プログラムの作り方です．
* [TestTask.java](./TestTask.java)
    + タイトル：Tickごとの処理(ゲーム全体)
    + 説明：仮想環境の毎回の物理計算後に
      確実に処理を行わせる場合のサンプル
      プログラムで，ゲーム全体で処理するのに
      適した書き方です．
* [TestPostSimulation.java](./TestPostSimulation.java)
    + タイトル：Tickごとの処理(オブジェクトごと)
    + 説明：仮想環境の毎回の物理計算後に
      確実に処理を行わせる場合のサンプル
      プログラムで，個々のゲームオブジェクトごとに
      個別の処理を書くのに適した方法です．
* [TestNoGraphics.java](./TestNoGraphics.java)
    + タイトル：グラフィックス無し
    + 説明：グラフィカルな表示をせずに
      物理シミュレーションだけを行う場合の
      サンプルです．
