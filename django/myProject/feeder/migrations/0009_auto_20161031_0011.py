# -*- coding: utf-8 -*-
# Generated by Django 1.10.2 on 2016-10-31 00:11
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('feeder', '0008_auto_20161030_2356'),
    ]

    operations = [
        migrations.AlterField(
            model_name='course',
            name='semester',
            field=models.CharField(choices=[('Aut', 'Autumn'), ('Spr', 'Spring'), ('Sum', 'Summer')], max_length=3),
        ),
    ]
