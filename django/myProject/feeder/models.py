import datetime
from django.db import models
from django.utils import timezone
from django.forms import ModelForm

class Admin(models.Model):
	username = models.CharField(max_length=20,default='')
	password = models.CharField(max_length=20,default='')
	loggedin = models.BooleanField(default=False)
	def __str__(self):
		return self.username

class Instructor(models.Model):
	name = models.CharField(max_length=30,default='')
	username = models.CharField(max_length=20,default='')
	email_id = models.EmailField(max_length=30,default='')
	password = models.CharField(max_length=20,default='')
	loggedin = models.BooleanField(default=False)
	admin = models.ForeignKey(Admin, on_delete=models.CASCADE, null=True)
	def __str__(self):
		return self.name

SEM_CHOICES = (
	('Autumn','Autumn'),
	('Spring','Spring'),
	('Summer','Summer')
)
class Course(models.Model):
	course_name = models.CharField(max_length=50,default='')
	course_code = models.CharField(max_length=10,default='')	
	instructor = models.ManyToManyField(Instructor)
	admin = models.ForeignKey(Admin, on_delete=models.CASCADE, null=True)
	semester = models.CharField(max_length=6,choices = SEM_CHOICES)
	year = models.IntegerField(default=0)
	def __str__(self):
		return self.course_name

class Student(models.Model):
	name = models.CharField(max_length=30,default='')
	email_id = models.EmailField(max_length=30,default='')
	password = models.CharField(max_length=20,default='')
	rollnumber= models.CharField(max_length=9,default='')
	courses = models.ManyToManyField(Course)
	loggedin = models.BooleanField(default=False)
	admin = models.ForeignKey(Admin, on_delete=models.CASCADE, null=True)
	def __str__(self):
		return self.name

class Feedback(models.Model):
	feedback_name = models.CharField(max_length = 20,default='')
	deadline_date = models.DateField(default=datetime.date.today)
	deadline_time = models.TimeField(default='23:59')
	course = models.ForeignKey(Course, on_delete=models.CASCADE, default='')
	def __str__(self):
		return self.feedback_name
	def is_due(self):
		if self.deadline_date >= datetime.datetime.now().date():
			if self.deadline_time >= datetime.datetime.now().time():
				return True
		return False

class RatingQuestion(models.Model):
	question = models.CharField(max_length = 150,default="")
	feedback = models.ForeignKey(Feedback, on_delete=models.CASCADE)
	def __str__(self):
		return self.question

class RatingAnswer(models.Model):
	question = models.ForeignKey(RatingQuestion, on_delete=models.CASCADE)
	answer_rated = models.IntegerField(default = 3)
	student = models.ForeignKey(Student)

class SubjectiveQuestion(models.Model):
	question = models.CharField(max_length = 150,default="")
	feedback = models.ForeignKey(Feedback, on_delete=models.CASCADE)
	def __str__(self):
		return self.question

class SubjectiveAnswer(models.Model):
	question = models.ForeignKey(SubjectiveQuestion, on_delete=models.CASCADE)
	answer = models.CharField(max_length= 300, default = "")
	student = models.ForeignKey(Student)

class Deadline(models.Model):
	course = models.ForeignKey(Course, on_delete=models.CASCADE, default='')
	assignment_name = models.CharField(max_length = 20,default='')
	deadline_date = models.DateField(default=datetime.date.today)
	deadline_time = models.TimeField(default='23:59')
	def __str__(self):
		return self.assignment_name
	def is_due(self):
		if self.deadline_date >= datetime.datetime.now().date():
			if self.deadline_time >= datetime.datetime.now().time():
				return True
		return False