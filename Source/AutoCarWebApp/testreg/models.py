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
    DateOfBirth = models.DateTimeField('birth date')
    Nationality = models.CharField(max_length = 100)
    ID_TYPES = (
        ('A', 'Aadhaar'),
        ('P', 'PAN'),
    )
    IDType = models.CharField(max_length = 1, choices=ID_TYPES)
    UniqueID = models.CharField(max_length = 50)
    TestID = models.IntegerField(default = 0)
    def __str__(self):
        return self.FirstName
    
class Test(models.Model):
    TestID = models.ForeignKey(Applicant)
    TestResult = models.IntegerField(default = 0)
    TestData = models.CharField(max_length = 2000)
    