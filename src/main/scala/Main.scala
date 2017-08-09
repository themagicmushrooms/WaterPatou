package fr.game.pkg

import com.badlogic.gdx.backends.lwjgl._
import com.badlogic.gdx.graphics.VertexAttributes.Usage
import com.badlogic.gdx.{Game, Gdx}
import com.badlogic.gdx.graphics.{Color, GL20, PerspectiveCamera, Texture}
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.{Material, Model, ModelBatch, ModelInstance}
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder

class MyGame extends Game {

  var batch: SpriteBatch = null
  var modelBatch: ModelBatch = null
  var img: Texture = null
  var cam: PerspectiveCamera = null
  var model: Model = null
  var modelInstance: ModelInstance = null

  override def create() = {
    batch = new SpriteBatch
    modelBatch = new ModelBatch
    img = new Texture("data/badlogic.jpg")
    setupCam
    val builder = new ModelBuilder
    model = builder.createBox(5f, 5f, 5f,
      new Material(ColorAttribute.createDiffuse(Color.GREEN)),
      Usage.Position | Usage.Normal)
    modelInstance = new ModelInstance(model)
  }

  private def setupCam = {
    cam = new PerspectiveCamera(90, Gdx.graphics.getWidth, Gdx.graphics.getHeight)
    cam.position.set(10f, 10f, 10f)
    cam.lookAt(0f, 0f, 0f)
    cam.near = 1f
    cam.far = 300f
    cam.update()
  }

  override def render() = {
    /*
    Gdx.gl.glClearColor(1, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    batch.begin()
    batch.draw(img, 0, 0)
    batch.end()
    */
    Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth, Gdx.graphics.getHeight)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT)
    modelBatch.begin(cam)
    modelBatch.render(modelInstance)
    modelBatch.end()
  }

  override def dispose() = {
    batch.dispose()
    modelBatch.dispose()
    img.dispose()
    model.dispose()
  }

}

object Main extends App {
  val cfg = new LwjglApplicationConfiguration
  cfg.title = "My Game"
  cfg.height = 480
  cfg.width = 800
  cfg.forceExit = false
  new LwjglApplication(new MyGame, cfg)
}
