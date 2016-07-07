from django.shortcuts import get_object_or_404, render
from django.http import HttpResponseRedirect, HttpResponse
from django.core.urlresolvers import reverse
from django import forms
from django.db import models
from testreg.models import Applicant

# Create your views here.
from django.http import HttpResponse
from pip._vendor import requests

def index(request):
	return render(request, 'testreg/welcome.html')

def newreg(request):
	return render(request, 'testreg/new_registration.html')

def newapl(request):
	fname = request.POST['first_name']
	lname = request.POST['last_name']
	addr = request.POST['address']
	sex = request.POST['sex']
	bdate = request.POST['bdate']
	nationality = request.POST['nation']
	idtype = request.POST['idtype']
	id = request.POST['uid']
	new_applicant = Applicant(FirstName=fname, LastName=lname, Address=addr, Sex=sex, Nationality=nationality, IDType=idtype, UniqueID=id, DateOfBirth=bdate)
	new_applicant.save()
	return HttpResponseRedirect(reverse('testreg:detail', args=(new_applicant.id,)))

def listreg(request):
	all_applicants = Applicant.objects.all()
	context = {'all_applicants': all_applicants}
	return render(request, 'testreg/list_applicants.html', context)

def findreg(request):
	return HttpResponse("Tell me whom to find")

def detail(request, id):
	applicant = get_object_or_404(Applicant, id=id)
	context = {'applicant': applicant}
	return render(request, 'testreg/applicant_details.html', context)

def deletereg(request, id):
	applicant = get_object_or_404(Applicant, id=id)
	applicant.delete()
	return HttpResponseRedirect(reverse('testreg:index'))
