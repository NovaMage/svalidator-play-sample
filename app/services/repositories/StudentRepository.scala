package services.repositories

import java.util.concurrent.atomic.AtomicLong

import models.domain.Student
import models.forms.{StudentCreateForm, StudentUpdateForm}

object StudentRepository {


  private val students = scala.collection.mutable.HashMap[Long, Student]()
  private val idGenerator = new AtomicLong(1L)

  def get(id: Long): Option[Student] = students.collectFirst {
    case (identifier, student) if id == identifier => student
  }

  def getAll: List[Student] = students.values.toList

  def create(form: StudentCreateForm): Unit = {
    val record = Student(idGenerator.getAndIncrement(),
      form.firstName,
      form.lastName,
      form.birthDate,
      form.gender,
      form.phone,
      form.foreigner,
      form.notes,
      form.address)
    students.+=(record.id -> record)
  }

  def update(form: StudentUpdateForm): Unit = {
    val record = Student(form.id,
      form.firstName,
      form.lastName,
      form.birthDate,
      form.gender,
      form.phone,
      form.foreigner,
      form.notes,
      form.address)
    students.update(record.id, record)
  }

  def delete(id: Long): Unit = {
    students.remove(id)
  }

  def hasWithNames(firstName: String,
                   lastName: String): Boolean = {
    students.exists { case (_, student) => student.firstName == firstName && student.lastName == lastName }
  }

  def hasWithNamesWithOtherId(idToExclude: Long, firstName: String, lastName: String): Boolean = {
    students.exists { case (id, student) => id != idToExclude && student.firstName == firstName && student.lastName == lastName }
  }

}
