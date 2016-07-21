from django.shortcuts import get_object_or_404, render, render_to_response
from django.http import HttpResponseRedirect, HttpResponse
from django.core.urlresolvers import reverse
from django import forms
from django.db import models
from django.template import RequestContext
from datetime import datetime
from testreg.models import Applicant
from testreg.models import Test
from testreg.forms import TestDataForm

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
	new_applicant.BiometricID = new_applicant.id + 12345678;
	new_applicant.save()
	return HttpResponseRedirect(reverse('testreg:detail', args=(new_applicant.id,)))

def listreg(request):
	all_applicants = Applicant.objects.all()
	context = {'all_applicants': all_applicants}
	return render(request, 'testreg/list_applicants.html', context)

def findreg(request):
	return render(request, 'testreg/find_applicant.html')

def detail(request, id):
	applicant = get_object_or_404(Applicant, id=id)
	context = {'applicant': applicant}
	return render(request, 'testreg/applicant_details.html', context)

def deletereg(request, id):
	applicant = get_object_or_404(Applicant, id=id)
	applicant.delete()
	return HttpResponseRedirect(reverse('testreg:index'))

def updatereg(request, id):
	applicant = get_object_or_404(Applicant, id=id)
	context = {'applicant': applicant}
	return render(request, 'testreg/modify_details.html', context)
	
def modifyreg(request, id):
	applicant = get_object_or_404(Applicant, id=id)
	applicant.FirstName = request.POST['first_name']
	applicant.LastName = request.POST['last_name']
	applicant.Address = request.POST['address']
	Applicant.Sex = request.POST['sex']
	Applicant.DateOfBirth = request.POST['bdate']
	Applicant.Nationality = request.POST['nation']
	Applicant.IDType = request.POST['idtype']
	Applicant.UniqueID = request.POST['uid']
	applicant.save()
	return HttpResponseRedirect(reverse('testreg:detail', args=(id,)))

def searchid(request, id):
	try:
		applicant = Applicant.objects.get(BiometricID=id)
		context = {'applicant': applicant}
		return render(request, 'testreg/applicant_details.html', context)
	except Applicant.DoesNotExist:
		return render(request, 'testreg/applicant_details.html')

def searchreg(request):
	fname = request.POST['first_name']
	lname = request.POST['last_name']
	bdate = request.POST['bdate']
	uid = request.POST['uid']
	applicants = Applicant.objects.filter(FirstName__startswith=fname).filter(LastName__startswith=lname).filter(UniqueID__startswith=uid)
	context = {'all_applicants': applicants}
	return render(request, 'testreg/list_applicants.html', context)

def get_testdata(request, id):
	applicant = get_object_or_404(Applicant, id=id)
	documents = Test.objects.all()
	
	form = TestDataForm
	# Render list page with the documents and the form
	return render_to_response(
		'testreg/get_testdata.html',
		{'documents': documents, 'form': form, 'applicant': applicant},
		context_instance=RequestContext(request)
	)	
	
def save_testdata(request, id):
	applicant = get_object_or_404(Applicant, id=id)
	context = {'applicant': applicant}
	
	if request.method == 'POST':
		form = TestDataForm(request.POST, request.FILES)
		if form.is_valid():
			test = Test(TestData = request.FILES['TestData'])
			test.TestDate = datetime.now()
			test.TestResult = 'P'
			test.applicant = applicant
			test.save()
			return HttpResponseRedirect(reverse('testreg:detail', args=(id,)))
		else:
			return HttpResponseRedirect(reverse('testreg:index'))
	else:
		return render(request, 'testreg/get_testdata.html', context)
		
	