# -*- coding: utf-8 -*-
from django import forms

class TestDataForm(forms.Form):
    TestData = forms.FileField(
        label='Select a file',
        help_text='max. 2 megabytes'
    )