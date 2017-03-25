from django import forms
import re
import csv
import datetime
from .models import Instructor,Course
# class AddFeedbackForm(forms.Form):
#     # course_code = forms.
#     feedback_name = forms.CharField(label='Feedback name', max_length=20)
#     deadline = forms.DateTimeField(initial=datetime.date.today)

class CourseForm(forms.Form):
	SEM_CHOICES = (
		('Autumn','Autumn'),
		('Spring','Spring'),
		('Summer','Summer')
	)
	course_name = forms.CharField(max_length=50, widget=forms.TextInput(attrs={'class':'form-control',
																				'placeholder':'Enter Course Name'}))
	course_code = forms.CharField(max_length=10, widget=forms.TextInput(attrs={'class':'form-control',
																				'placeholder':'Enter Course Code'}))	
	instructors = forms.ModelMultipleChoiceField(queryset=Instructor.objects.all(),
											widget=forms.CheckboxSelectMultiple(attrs={'class':'checkbox'}))
	semester = forms.ChoiceField(widget=forms.Select(attrs={'class':'form-control'}),choices = SEM_CHOICES)
	year = forms.IntegerField(widget=forms.NumberInput(attrs={'class':'form-control'}),initial=2016)
	midsem_date = forms.DateField(initial=datetime.date.today)
	endsem_date = forms.DateField(initial=datetime.date.today)
	docfile = forms.FileField(
        label='Select a .csv file with list of students to enroll with following column order:name,rollno(username),password,email-id',
        help_text='max. 42 megabytes'
    )
	def is_valid(self):
		valid = super(CourseForm, self).is_valid()
		if not valid:
			return valid
		if(Course.objects.filter(course_code=self.cleaned_data["course_code"]).exists()):
			self._errors["course_code"] = "Course Code already exists"
			return False
		if not self.cleaned_data["docfile"].name.endswith('.csv'):
			self._errors["docfile"] = "* Invalid Input Format"
			return False
		f = self.cleaned_data["docfile"]
		filepath = '/tmp/somefile.csv'
		with open(filepath, 'wb+') as dest:
			for chunk in f.chunks():
				dest.write(chunk)
		with open(filepath) as csvfile:
			readCSV = csv.reader(csvfile, delimiter=',')
			for row in readCSV:
				if(len(row)!=4):
					self._errors["docfile"] = "* Invalid Input Format"
					return False
		return True

class InstructorForm(forms.Form):
	name = forms.CharField(max_length=30, widget=forms.TextInput(attrs={'class':'form-control',
																				'placeholder':'Enter Your Name'}))
	username = forms.CharField(max_length=30, widget=forms.TextInput(attrs={'class':'form-control',
																				'placeholder':'Enter Username'}))
	email_id = forms.EmailField(max_length=30, widget=forms.TextInput(attrs={'class':'form-control',
																				'placeholder':'Enter email id'}))
	password = forms.CharField(max_length=20,widget=forms.PasswordInput(attrs={'class':'form-control',
																				'placeholder':'Enter Password'}))
	reenter_password = forms.CharField(label="Re-Enter Password", max_length=20,widget=forms.PasswordInput(attrs={'class':'form-control',
																				'placeholder':'Re-Enter Password'}))
	def is_valid(self):
		valid = super(InstructorForm, self).is_valid()
		if not valid:
			return valid
		if(Instructor.objects.filter(username=self.cleaned_data["username"]).exists()):
			self._errors["username"] = "Username already exists"
			return False
		if(Instructor.objects.filter(email_id=self.cleaned_data["email_id"]).exists()):
			self._errors["email_id"] = "Email id already used"
			return False
		if(self.cleaned_data["password"]!=self.cleaned_data["reenter_password"]):
			self._errors["reenter_password"]= "Password does not match"
			return False
		return True	

class DeadlineForm(forms.Form):
	course = forms.ModelChoiceField(queryset=Course.objects.all(),empty_label=None,
														widget=forms.Select(attrs={'class':'form-control'}))
	assignment_name = forms.CharField(max_length=30, widget=forms.TextInput(attrs={'class':'form-control',
																				'placeholder':'Enter Particular Name'}))
	deadline_date = forms.DateField(initial=datetime.date.today)
	deadline_time = forms.TimeField(initial="23:59")

class FeedbackForm(forms.Form):
	course = forms.ModelChoiceField(queryset=Course.objects.all(),empty_label=None,
														widget=forms.Select(attrs={'class':'form-control'}))
	feedback_name = forms.CharField(max_length=20, widget=forms.TextInput(attrs={'class':'form-control',
																				'placeholder':'Enter feedback Name'}))
	deadline_date = forms.DateField(initial=datetime.date.today)
	deadline_time = forms.TimeField(initial="23:59")
