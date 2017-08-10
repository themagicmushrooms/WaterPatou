package fr.game.pkg

import com.badlogic.gdx.math.{Plane, Vector3}
import com.badlogic.gdx.physics.bullet.Bullet
import com.badlogic.gdx.physics.bullet.collision._
import com.badlogic.gdx.physics.bullet.dynamics._
import com.badlogic.gdx.utils.Disposable

import scala.collection.mutable.ArrayBuffer

class Physics extends Disposable {

  var gravity = new Vector3(0f, 0f, -9.81f)
  val waterDensity = 1f
  val woodDensity = 0.5f

  Bullet.init()
  var broadPhase = new btDbvtBroadphase
  var collisionConfig = new btDefaultCollisionConfiguration
  var dispatcher = new btCollisionDispatcher(collisionConfig)
  var solver = new btSequentialImpulseConstraintSolver
  var world = new btDiscreteDynamicsWorld(dispatcher, broadPhase, solver, collisionConfig)
  world.setGravity(gravity)

  private val bodies = ArrayBuffer.empty[btRigidBody]

  def add(body: btRigidBody) {
    world.addRigidBody(body)
    bodies.append(body)
  }

  def step(delta: Float) = {
    world.stepSimulation(delta, 5, 1f / 60f)
    for (body <- bodies.filter(isUnderwater)) {
      applyArchimedes(body)
      applyDrag(body)
    }
  }

  def isUnderwater(body: btRigidBody) = {
    val v = new Vector3
    body.getWorldTransform.getTranslation(v)
    v.z < 0f
  }

  def applyArchimedes(body: btRigidBody) = {
    val push = gravity.cpy().scl(-1f * waterDensity * volume(body))
    body.applyCentralForce(push)
  }

  def applyDrag(body: btRigidBody) = {
    val coef = surface(body)
    val speed = body.getLinearVelocity.len
    val drag = body.getLinearVelocity.cpy().scl(speed).scl(-1f * waterDensity * coef)
    body.applyCentralForce(drag)
  }

  def volume(body: btRigidBody) = { // approximation
    val from = new Vector3
    val to = new Vector3
    body.getAabb(from, to)
    Math.abs((from.x - to.x) * (from.y - to.y) * (from.z - to.z))
  }

  def surface(body: btRigidBody) = { // approximation
    val from = new Vector3
    val to = new Vector3
    body.getAabb(from, to)
    Math.abs((from.x - to.x) * (from.y - to.y))
  }

  override def dispose() = {
  }

}
