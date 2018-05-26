package services

import java.util.concurrent.atomic.AtomicLong

import models.domain.Student
import models.forms.{StudentCreateForm, StudentUpdateForm}

object StudentRepository {

  private val students = scala.collection.mutable.HashMap[Long, Student]()
  private val idGenerator = new AtomicLong(1L)

  def getAll: List[Student] = students.values.toList

  def create(form: StudentCreateForm): Unit = {
    val record = Student(idGenerator.getAndIncrement(),
      form.firstName,
      form.lastName,
      form.birthDate,
      form.gender,
      form.phone,
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
      form.notes,
      form.address)
    students.update(record.id, record)
  }

  def delete(id: Long): Unit = {
    students.remove(id)
  }

}
