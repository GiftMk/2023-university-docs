from tkinter import *
from tkinter.ttk import *

board = ['qwertyuiop', 'asdfghjkl', 'zxcvbnm']

window = Tk()

keyboard_input = Frame(window)
keyboard_input.grid(row=0)

keyboard = Frame(window)
keyboard.grid(row=1)

text = StringVar()
text.set('Hello there')

label = Label(keyboard_input, textvariable=text)
label.grid(row=0, column=0)

clear_button = Button(keyboard_input, text='Clear')
clear_button.grid(row=0, column=1)

for i, row in enumerate(board):
    for j, char in enumerate(row):
        button = Button(keyboard, text=char)
        button.grid(row=i, column=j)

window.mainloop()