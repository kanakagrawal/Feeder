from django.conf.urls import url, include
# from django.contrib import admin
from . import views

app_name = 'feeder'
urlpatterns = [
    url(r'^admin/$',views.admin,name='admin'),
    url(r'^admin/home$',views.adminpage,name='adminpage'),
    url(r'^admin/runningcourses$',views.runningcourses,name='runningcourses'),
    url(r'^admin/addcourse$',views.addcourse,name='addcourse'),
    url(r'^admin/logout$',views.adminlogout,name='adminlogout'),
    url(r'^admin/course/(?P<pk>.+)/$',views.deleteCourse,name='deleteCourse'),
    url(r'^login$',views.instructor_login, name = 'instructor_login'),
    url(r'^register$',views.instructor_register, name = 'instructor_register'),
    url(r'^instructor/(?P<instructor>.+)/$',views.instructor, name = 'instructor'),
    url(r'^instructor/(?P<instructor>.+)/logout$',views.instructorlogout, name = 'instructorlogout'),
    url(r'^instructor/(?P<instructor>.+)/addFeedback$',views.addFeedback,name='addFeedback'),
    url(r'^instructor/(?P<instructor>.+)/viewFeedback$',views.viewFeedback,name='viewFeedback'),
    url(r'^instructor/(?P<question_id>.+)/subjectiveFeedback$',views.subjectiveFeedback,name='subjectiveFeedback'),
    url(r'^instructor/(?P<question_id>.+)/ratingFeedback$',views.ratingFeedback,name='ratingFeedback'),
    url(r'^instructor/(?P<instructor>.+)/addDeadline$',views.addDeadline,name='addDeadline'),
    url(r'^instructor/(?P<instructor>.+)/viewDeadline$',views.viewDeadline,name='viewDeadline'),
    url(r'^instructor/(?P<instructor>.+)/viewFeedbackDeadline$',views.viewFeedbackDeadline,name='viewFeedbackDeadline'),
	url(r'^instructor/(?P<pk>.+)/(?P<instructor>.+)/deleteDeadline$',views.deleteDeadline,name='deleteDeadline'),
	url(r'^instructor/(?P<pk>.+)/(?P<instructor>.+)/deleteFeedbackDeadline$',views.deleteFeedbackDeadline,name='deleteFeedbackDeadline'),
    url(r'^auth/$', views.student_authenticate),
    url(r'^auth/rating/$', views.rating_capture),
    url(r'^auth/subjective/$', views.subjective_capture)

]
