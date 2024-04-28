from tkinter import *
from tkinter.ttk import *

window = Tk()
for label_num in range(6):
    button_text = f"Button {label_num}"
    row = label_num // 2
    col = label_num % 2
    button = Button(window, text=button_text)
    button.grid(row=row, column=col)
window.mainloop()