'''
About program: Doomsday algorithm in Python3 minified
@author: Ryo112358
'''
from pandas.tseries.util import isleapyear
from math import floor
print()
weekday = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday']
months = {1: 'January',
          2: 'February',
          3: 'March',
          4: 'April',
          5: 'May',
          6: 'June',
          7: 'July',
          8: 'August',
          9: 'September',
          10: 'October',
          11: 'November',
          12: 'December'}
print("Calculate the weekday of any date on the Gregorian calendar AD.")
print()
month = int(input("Enter the month (mm): "))
day = int(input("Enter the day (dd): "))
year = input("Enter the year (yyyy): ")
print()
LEAPYEAR = isleapyear(int(year))
anchorDay = int(year[0] + year[1])
if anchorDay % 4 == 0:
    anchorDay = 2
elif anchorDay % 4 == 1:
    anchorDay = 0
elif anchorDay % 4 == 2:
    anchorDay = 5
elif anchorDay % 4 == 3:
    anchorDay = 3
else:
    print("Year is not properly formatted.")
    quit()
print("Anchor day: " + weekday[anchorDay])
divByTwelve = floor(int(year[2] + year[3])/12)
remainder = int(year[2] + year[3]) % 12
divByFour = floor(remainder/4)
total = divByTwelve + remainder + divByFour + anchorDay
doomsday = total % 7
print("Doomsday: " + weekday[doomsday])
dayOfWeek = 0
if month == 1:
    if LEAPYEAR == True:
        dayOfWeek = 4 - day
    else:
        dayOfWeek = 3 - day
elif month == 2:
    if LEAPYEAR == True:
        dayOfWeek = 29 - day
    else:
        dayOfWeek = 28 - day
elif month == 3:
    dayOfWeek = 7 - day
elif month == 4:
    dayOfWeek = 4 - day
elif month == 5:
    dayOfWeek = 9 - day
elif month == 6:
    dayOfWeek = 6 - day
elif month == 7:
    dayOfWeek = 11 - day
elif month == 8:
    dayOfWeek = 8 - day
elif month == 9:
    dayOfWeek = 5 - day
elif month == 10:
    dayOfWeek = 10 - day
elif month == 11:
    dayOfWeek = 7 - day
else:
    dayOfWeek = 12 - day
index = -(dayOfWeek % 7)
print(months[month] + " "+ str(day) + ", " + str(year) + ": " + weekday[(doomsday + index) % 7])
