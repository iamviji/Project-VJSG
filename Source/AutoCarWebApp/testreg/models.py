from django.db import models
from django.utils.encoding import python_2_unicode_compatible


# Create your models here.
@python_2_unicode_compatible  # only if you need to support Python 2
class Applicant(models.Model):
    FirstName = models.CharField(max_length = 100)
    LastName = models.CharField(max_length = 100)
    Address = models.CharField(max_length = 300)
    SEX_TYPES = (
        ('M', 'Male'),
        ('F', 'Female'),
        ('O', 'Other')
    )
    Sex = models.CharField(max_length = 1, choices=SEX_TYPES, default='M')
    DateOfBirth = models.DateField('birth date')
    Nationality = models.CharField(max_length = 100)
    ID_TYPES = (
        ('A', 'Aadhaar'),
        ('P', 'PAN'),
        ('O', 'Other')
    )
    IDType = models.CharField(max_length = 1, choices=ID_TYPES)
    UniqueID = models.CharField(max_length = 50)
    BiometricID = models.IntegerField(default = 0)
    def __str__(self):
        return self.FirstName

def user_directory_path(instance, filename):
    applicant = instance.applicant
    user = applicant.FirstName + "_" + applicant.LastName + "_" + str(applicant.id)
    return 'user_{0}/{1}/{2}'.format(user, instance.id, filename)
    
class Test(models.Model):
    applicant = models.ForeignKey(Applicant, on_delete=models.CASCADE)
    TestDate = models.DateTimeField('Test date')
    RESULT_TYPES = (
        ('P', 'PASS'),
        ('F', 'FAIL'),
        ('C', 'CANCELLED')
    )
    TestResult = models.CharField(max_length = 1, choices=RESULT_TYPES)
    TestData = models.FileField(upload_to=user_directory_path)
    