from django.shortcuts import render, get_object_or_404
from django.http import HttpResponse, HttpResponseRedirect
from .models import Instructor, Admin, Course, Feedback, Student, Deadline, RatingQuestion, SubjectiveQuestion, RatingAnswer,SubjectiveAnswer
from .forms import CourseForm, InstructorForm, DeadlineForm, FeedbackForm
from django.urls import reverse
from django.template import loader
import csv
import datetime
from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response
from feeder.serializers import StudentSerializer,StudentModelSerializer


@api_view(['POST'])
def student_authenticate(request):
    if request.method == 'POST':
       	try:
       		s = Student.objects.get(rollnumber=request.POST['rollnumber'],password=request.POST['password'])
       		se = StudentModelSerializer(s)
       		return Response(se.data, status=status.HTTP_201_CREATED)
       	except:
       		return Response(status=status.HTTP_400_BAD_REQUEST)		
    return Response(status=status.HTTP_400_BAD_REQUEST)

@api_view(['POST'])
def rating_capture(request):
	if request.method=='POST':
		try:
			student = Student.objects.get(id=request.POST["student_id"])
			question = RatingQuestion.objects.get(id=request.POST["question_id"])
			answer = RatingAnswer(student=student,question=question,answer_rated=request.POST["answer"])
			answer.save();
			return Response( status=status.HTTP_201_CREATED)
		except:
			return Response( status=status.HTTP_400_BAD_REQUEST)
	else:
		return Response(status=status.HTTP_400_BAD_REQUEST)

@api_view(['POST'])
def subjective_capture(request):
	if request.method=='POST':
		try:
			student = Student.objects.get(id=request.POST["student_id"])
			question = SubjectiveQuestion.objects.get(id=request.POST["question_id"])
			answer = SubjectiveAnswer(student=student,question=question,answer=request.POST["answer"])
			answer.save();
			return Response( status=status.HTTP_201_CREATED)
		except:
			return Response( status=status.HTTP_400_BAD_REQUEST)
	else:
		return Response(status=status.HTTP_400_BAD_REQUEST)

def adminlogout(request):
	admin = Admin.objects.get(username='admin')
	admin.loggedin = False
	admin.save()
	return HttpResponseRedirect(reverse('feeder:admin'))

def admin(request):
	admin = Admin.objects.get(username='admin')
	if admin.loggedin :
		return HttpResponseRedirect(reverse('feeder:adminpage'))
	if(request.method == 'POST'):
		try:
			admin = Admin.objects.get(username=request.POST['username'])
		except (KeyError, Admin.DoesNotExist):
			context = { 'error_message': "Incorrect username or password." }
			return render(request, 'feeder/admin_login.html', context)
		if(admin.password == request.POST['password']):
			admin.loggedin = True
			admin.save()
			return HttpResponseRedirect(reverse('feeder:adminpage'))
		else:
	        	context={ 'error_message': "Incorrect username or password." }
	        	return render(request, 'feeder/admin_login.html', context)
	else:
		return render(request,'feeder/admin_login.html')

def adminpage(request):
	admin = Admin.objects.get(username='admin')
	if admin.loggedin :
		return render(request, 'feeder/adminpage.html')
	else:
		return HttpResponseRedirect(reverse('feeder:admin'))

def runningcourses(request):
	admin = Admin.objects.get(username="admin")
	if admin.loggedin:
		context = {'admin' : admin}
		return render(request,'feeder/runningcourses.html',context)
	else:
		return HttpResponseRedirect(reverse('feeder:admin'))	

def addcourse(request):
	admin = Admin.objects.get(username='admin')	
	if admin.loggedin :
		if request.method == 'POST':
			form = CourseForm(request.POST,request.FILES)
			if form.is_valid():
				code = request.POST["course_code"]
				name = request.POST["course_name"]
				Sem = request.POST["semester"]
				Year = request.POST["year"]
				ids = request.POST.getlist("instructors")
				midsem_date = request.POST["midsem_date"]
				endsem_date = request.POST["endsem_date"]
				c = Course(course_name=name,course_code=code,admin=Admin.objects.get(username='admin'),semester=Sem,year=Year)
				c.save()
				print(ids)
				for i in ids:
					instructor = Instructor.objects.get(id=i)
					c.instructor.add(instructor)
					c.save()
				midsem = Feedback(feedback_name = "Midsem Feedback", deadline_date = midsem_date, course = c,)
				midsem.save()
				endsem = Feedback(feedback_name = "Endsem Feedback", deadline_date = endsem_date, course = c)
				endsem.save()
				q1=RatingQuestion(question="How was the instructor's teaching?",feedback=midsem)
				q2=RatingQuestion(question="How useful was the course content?",feedback=midsem)
				q1.save()
				q2.save()
				q1=RatingQuestion(question="How was the instructor's teaching?",feedback=endsem)
				q2=RatingQuestion(question="How useful was the course content?",feedback=endsem)
				q1.save()
				q2.save()
				midsem = Deadline(course = c,assignment_name = "Midsem Exam", deadline_date = midsem_date)
				endsem = Deadline(course = c,assignment_name = "Endsem Exam", deadline_date = endsem_date)
				midsem.save()
				endsem.save()
				f = request.FILES['docfile']
				filepath = '/tmp/somefile.csv'
				with open(filepath, 'wb+') as dest:
					for chunk in f.chunks():
						dest.write(chunk)
				with open(filepath) as csvfile:
					readCSV = csv.reader(csvfile, delimiter=',')
					for row in readCSV:
						name = row[0]
						rollno = row[1]
						password = row[2]
						email_id = row[3]
						try:
							s = Student.objects.get(rollnumber=rollno)
						except:
							s = Student(name=name,email_id=email_id,password=password,rollnumber=rollno)
							s.save()
						s.courses.add(c)
						s.save()
				context = { 'admin': admin }
				return HttpResponseRedirect(reverse('feeder:adminpage'))
		else:
			form = CourseForm()
		admin = Admin.objects.get(username="admin")
		context = {'admin' : admin,"form":form}
		return render(request,'feeder/addcourse.html',context)
	else:
		return HttpResponseRedirect(reverse('feeder:admin'))

def deleteCourse(request,pk):
	c = Course.objects.get(pk=pk)
	c.delete()
	return HttpResponseRedirect(reverse('feeder:runningcourses'))

def instructor_register(request):
	if request.method == 'POST':
		try:
			x = request.POST["social"]
			if(Instructor.objects.filter(email_id=request.POST["email_id"]).exists()):
				i = Instructor.objects.get(email_id=request.POST["email_id"])
				i.loggedin = True
				i.save()
				return HttpResponseRedirect(reverse('feeder:instructor', args=(i.username,)))
			else:
				name = request.POST['name']
				username = request.POST['username']
				email_id = request.POST['email_id']
				password = request.POST['password']
				i = Instructor(name=name,username=username,email_id=email_id,password=password,admin=Admin.objects.get(username='admin'))
				i.save()
				i.loggedin = True
				i.save()
				return HttpResponseRedirect(reverse('feeder:instructor', args=(i.username,)))
		except:	
			form = InstructorForm(request.POST)
			if form.is_valid():
				name = request.POST['name']
				username = request.POST['username']
				email_id = request.POST['email_id']
				password = request.POST['password']
				i = Instructor(name=name,username=username,email_id=email_id,password=password,admin=Admin.objects.get(username='admin'))
				i.save()
				i.loggedin = True
				i.save()
				return HttpResponseRedirect(reverse('feeder:instructor', args=(i.username,)))
	else:
		form = InstructorForm()
	context = {"form":form}
	return render(request,'feeder/instructor_register.html',context)

def instructor_login(request):
	if request.method == 'POST':
		try:
			selected_instructor = Instructor.objects.get(username=request.POST['username'])
		except (KeyError, Instructor.DoesNotExist):
			context = { 'error_message': "Incorrect username or password." }
			return render(request, 'feeder/instructor_login.html', context)
		else:
			if(selected_instructor.password == request.POST['password']):
				selected_instructor.loggedin = True
				selected_instructor.save()
				return HttpResponseRedirect(reverse('feeder:instructor', args=(selected_instructor.username,)))	
			else:
		        	context={
		       	 		'error_message': "Incorrect username or password.",
		        	}
		        	return render(request, 'feeder/instructor_login.html', context)
	else:
		return render(request, 'feeder/instructor_login.html')

def instructor(request,instructor):
	selected_instructor = get_object_or_404(Instructor,username=instructor)
	if selected_instructor.loggedin :
		# selected_instructor = Instructor.objects.get(username=instructor)
		context = { 'instructor' : selected_instructor}
		return render(request, 'feeder/instructorpage.html',context)
	else:
		return HttpResponseRedirect(reverse('feeder:instructor_login'))

def addFeedback(request, instructor):
	selected_instructor = Instructor.objects.get(username=instructor)
	if selected_instructor.loggedin:
		if request.method == 'POST':
			form = FeedbackForm(request.POST)
			if form.is_valid() and (Feedback.objects.filter(feedback_name=request.POST["feedback_name"],course=Course.objects.get(id=request.POST['course'])).exists()==False):
				i = request.POST['course']
				feedback_name = request.POST['feedback_name']
				deadline_date = request.POST['deadline_date']
				deadline_time = request.POST['deadline_time']
				f = Feedback(feedback_name=feedback_name,deadline_time=deadline_time,deadline_date=deadline_date,course=Course.objects.get(id=i))
				f.save()
				i = request.POST['ratedQuestions']
				l = 0
				while l < int(i):
					l = l+1
					question = request.POST['rated'+str(l)]
					x = RatingQuestion(question=question,feedback=f)
					x.save()
				i = request.POST['subjectiveQuestions']
				l = 0
				while l < int(i):
					l = l+1
					question = request.POST['subjective'+str(l)]
					x = SubjectiveQuestion(question=question,feedback=f)
					x.save()
				return HttpResponseRedirect(reverse('feeder:instructor', args=(selected_instructor.username,)))
		else:
			form = FeedbackForm()
		form.fields['course'].queryset=selected_instructor.course_set.all()
		context = { 'form' : form, 'instructor' : selected_instructor}
		return render(request, 'feeder/addFeedback.html',context)
	else:
		return HttpResponseRedirect(reverse('feeder:instructor_login'))

def viewFeedback(request, instructor):
	selected_instructor = Instructor.objects.get(username=instructor)
	if selected_instructor.loggedin:
		context = { 'instructor' : selected_instructor}
		return render(request, 'feeder/viewFeedback.html',context)
	else:
		return HttpResponseRedirect(reverse('feeder:instructor_login'))

def subjectiveFeedback(request,question_id):
	question = SubjectiveQuestion.objects.get(id=question_id)
	context = {'question':question}
	return render(request, 'feeder/subjectiveFeedback.html',context)

def ratingFeedback(request,question_id):
	question = RatingQuestion.objects.get(id=question_id)
	import matplotlib
	from matplotlib.dates import DateFormatter
	import numpy as np
	import matplotlib.pyplot as plt
	from matplotlib.backends.backend_agg import FigureCanvasAgg as FigureCanvas
	from matplotlib.figure import Figure
	data=[0,0,0,0,0]
	for answer in question.ratinganswer_set.all():
		data[answer.answer_rated-1] = data[answer.answer_rated-1]+1
	print(data)
	fig = Figure(figsize=(2,2))
	ax=fig.add_subplot(1,1,1)
	cols = ['red','orange','yellow','green','blue','purple','indigo']*10
	ind = [1,2,3,4,5]
	cols = cols[0:len(ind)]
	ax.bar(ind, data,color=cols)
	canvas = FigureCanvas(fig)
	response = HttpResponse(content_type='image/png')
	canvas.print_png(response)
	return response

	# context = {'question':question}
	# return render(request, 'feeder/subjectiveFeedback.html',context)


def addDeadline(request, instructor):
	selected_instructor = Instructor.objects.get(username=instructor)
	if selected_instructor.loggedin:
		if request.method == 'POST':
			form = DeadlineForm(request.POST)
			if form.is_valid() and (Deadline.objects.filter(assignment_name=request.POST["assignment_name"],course=Course.objects.get(id=request.POST['course'])).exists()==False):
				i = request.POST['course']
				assignment_name = request.POST['assignment_name']
				deadline_date = request.POST['deadline_date']
				deadline_time = request.POST['deadline_time']
				a = Deadline(course=Course.objects.get(id=i),assignment_name=assignment_name,deadline_time=deadline_time,deadline_date=deadline_date)
				a.save()
				return HttpResponseRedirect(reverse('feeder:instructor', args=(selected_instructor.username,)))
		else:
			form = DeadlineForm()
		form.fields['course'].queryset=selected_instructor.course_set.all()
		context={'form':form,'instructor':selected_instructor}	
		return render(request,'feeder/addDeadline.html',context)
	else:
		return HttpResponseRedirect(reverse('feeder:instructor_login'))

def viewDeadline(request, instructor):
	selected_instructor = Instructor.objects.get(username=instructor)
	if selected_instructor.loggedin:
		context={'instructor':selected_instructor}
		return render(request,'feeder/viewDeadline.html',context)
	else:
		return HttpResponseRedirect(reverse('feeder:instructor_login'))

def viewFeedbackDeadline(request, instructor):
	selected_instructor = Instructor.objects.get(username=instructor)
	if selected_instructor.loggedin:
		context={'instructor':selected_instructor}
		return render(request,'feeder/viewFeedbackDeadline.html',context)
	else:
		return HttpResponseRedirect(reverse('feeder:instructor_login'))

def deleteFeedbackDeadline(request,pk,instructor):
	selected_instructor = Instructor.objects.get(username=instructor)
	if selected_instructor.loggedin:
		f = Feedback.objects.get(pk=pk)
		f.delete()
		return HttpResponseRedirect(reverse('feeder:viewFeedbackDeadline', args=(selected_instructor.username,)))
	else:
		return HttpResponseRedirect(reverse('feeder:instructor_login'))

def deleteDeadline(request,pk,instructor):
	selected_instructor = Instructor.objects.get(username=instructor)
	if selected_instructor.loggedin:
		d = Deadline.objects.get(pk=pk)
		d.delete()
		return HttpResponseRedirect(reverse('feeder:viewDeadline', args=(selected_instructor.username,)))
	else:
		return HttpResponseRedirect(reverse('feeder:instructor_login'))

def instructorlogout(request,instructor):
	instructor = Instructor.objects.get(username=instructor)
	instructor.loggedin = False
	instructor.save()
	return HttpResponseRedirect(reverse('feeder:instructor_login'))