package fr.game.pkg

import com.badlogic.gdx.math.{Plane, Vector3}
import com.badlogic.gdx.physics.bullet.Bullet
import com.badlogic.gdx.physics.bullet.collision._
import com.badlogic.gdx.physics.bullet.dynamics._
import com.badlogic.gdx.utils.Disposable

import scala.collection.mutable.ArrayBuffer

class Physics extends Disposable {

  Bullet.init()
  var broadPhase = new btDbvtBroadphase
  var collisionConfig = new btDefaultCollisionConfiguration
  var dispatcher = new btCollisionDispatcher(collisionConfig)
  var solver = new btSequentialImpulseConstraintSolver
  var world = new btDiscreteDynamicsWorld(dispatcher, broadPhase, solver, collisionConfig)
  world.setGravity(new Vector3(0f, 0f, -9.81f))

  private val bodies = ArrayBuffer.empty[btRigidBody]

  def add(body: btRigidBody) {
    world.addRigidBody(body)
    bodies.append(body)
  }

  def step(delta: Float) = {
    world.stepSimulation(delta, 5, 1f / 60f)
    bodies.filter(isUnderwater).foreach(applyArchimedes)
  }

  def isUnderwater(body: btRigidBody) = {
    val v = new Vector3
    body.getWorldTransform.getTranslation(v)
    v.z < 0f
  }

  def applyArchimedes(body: btRigidBody) = {
    body.applyCentralForce(new Vector3(0f, 0f, 2000f))
  }

  override def dispose() = {
  }

}
