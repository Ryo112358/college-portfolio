'''
Created on Dec 22, 2016

@author: Ryo112358

About program: Doomsday algorithm - Find the weekday of any date on the Gregorian calendar AD.
'''
from pandas.tseries.util import isleapyear
from math import floor

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

# -----------------------------------------------------------------------------

print("Calculate the weekday of any date on the Gregorian calendar AD.")
print()

month = int(input("Enter the month (mm): "))
day = int(input("Enter the day (dd): "))
year = input("Enter the year (yyyy): ")
print()

# Is the year a leap year?
LEAPYEAR = isleapyear(int(year))


# Anchor day for century
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


# How many times does 12 go into yy?
divByTwelve = floor(int(year[2] + year[3])/12)
#print(divByTwelve)

# Remainder of previous calculation
remainder = int(year[2] + year[3]) % 12
#print(remainder)

# How many times does 4 go into remainder?
divByFour = floor(remainder/4)


# Sum the four values
total = divByTwelve + remainder + divByFour + anchorDay
doomsday = total % 7

print("Doomsday: " + weekday[doomsday])
# -----------------------------------------------------------------------------

dayOfWeek = 0

if month == 1: #January
    if LEAPYEAR == True:
        dayOfWeek = 4 - day
    else:
        dayOfWeek = 3 - day
elif month == 2: #February
    if LEAPYEAR == True:
        dayOfWeek = 29 - day
    else:
        dayOfWeek = 28 - day
elif month == 3: #March
    dayOfWeek = 7 - day
elif month == 4: #April
    dayOfWeek = 4 - day
elif month == 5: #May
    dayOfWeek = 9 - day
elif month == 6: #June
    dayOfWeek = 6 - day
elif month == 7: #July
    dayOfWeek = 11 - day
elif month == 8: #August
    dayOfWeek = 8 - day
elif month == 9: #September
    dayOfWeek = 5 - day
elif month == 10: #October
    dayOfWeek = 10 - day
elif month == 11: #November
    dayOfWeek = 7 - day
else: #December
    dayOfWeek = 12 - day

index = -(dayOfWeek % 7)
print(months[month] + " "+ str(day) + ", " + str(year) + ": " + weekday[(doomsday + index) % 7])
