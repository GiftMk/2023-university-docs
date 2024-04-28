from tkinter import *
from tkinter.ttk import *

window = Tk()
side_labels = ["bottom1", "bottom2", "top1", "top2", "left1", "right1"]

for side_label in side_labels:
    button = Button(window, text=side_label)
    button.pack(side=side_label[:-1])

window.mainloop()