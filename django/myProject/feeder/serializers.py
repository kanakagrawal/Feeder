from rest_framework import serializers
from .models import Student,Course,Feedback,Deadline,SubjectiveQuestion,RatingQuestion,RatingAnswer,SubjectiveAnswer

class StudentSerializer(serializers.Serializer):
	rollnumber = serializers.CharField(required=True, max_length=9)	##username is same as roll number
	password = serializers.CharField(required=True, max_length=20)
	loggedin = serializers.BooleanField(required=False)
	secretKey = serializers.CharField(required=True, max_length=50)		##ensure only our app can send request.
	def create(self, validated_data):
		return Student.objects.create(**validated_data)

	def update(self, instance, validated_data):
		instance.rollnumber = validated_data.get('rollnumber', instance.rollnumber)
		instance.save()
		return instance

	def validate_loggedin(self,value):
		return True
	def validate_secretKey(self,value):
		if value != 'hello':
			raise serializers.ValidationError("Invalid access")
		return value

class SubjectiveQuestionSerializer(serializers.ModelSerializer):
	class Meta:
		model = SubjectiveQuestion
		fields = '__all__'

class SubjectiveAnswerSerializer(serializers.ModelSerializer):
	class Meta:
		model = SubjectiveAnswer
		fields = '__all__'	

class RatingAnswerSerializer(serializers.ModelSerializer):
	class Meta:
		model = RatingAnswer
		fields = '__all__'	

class RatingQuestionSerializer(serializers.ModelSerializer):
	class Meta:
		model = RatingQuestion
		fields = '__all__'

class FeedbackSerializer(serializers.ModelSerializer):
	subjectivequestion_set = SubjectiveQuestionSerializer(many=True)
	ratingquestion_set = RatingQuestionSerializer(many=True)
	class Meta:
		model = Feedback
		fields = '__all__'

class DeadlineSerializer(serializers.ModelSerializer):
	class Meta:
		model = Deadline
		fields = '__all__'		

class CourseSerializer(serializers.ModelSerializer):
	feedback_set = FeedbackSerializer(many=True)
	deadline_set = DeadlineSerializer(many=True)
	class Meta:
		model = Course
		fields = '__all__'

class StudentModelSerializer(serializers.ModelSerializer):
	courses = CourseSerializer(many=True)
	ratinganswer_set = RatingAnswerSerializer(many=True)
	subjectiveanswer_set = SubjectiveAnswerSerializer(many=True)	
	class Meta:
		model = Student
		fields = '__all__'