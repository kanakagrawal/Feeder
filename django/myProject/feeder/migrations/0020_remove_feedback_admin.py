# -*- coding: utf-8 -*-
# Generated by Django 1.10.2 on 2016-11-01 13:00
from __future__ import unicode_literals

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('feeder', '0019_auto_20161101_0107'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='feedback',
            name='admin',
        ),
    ]
