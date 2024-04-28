from tkinter import *
from tkinter.ttk import *

sample_text = """
It is a long established fact that a reader will be distracted 
by the readable content of a page when looking at its layout. 
The point of using Lorem Ipsum is that it has a more-or-less normal 
distribution of letters, as opposed to using 'Content here, content here', 
making it look like readable English. Many desktop publishing packages and 
web page editors now use Lorem Ipsum as their default model text, 
and a search for 'lorem ipsum' will uncover many web sites still in their infancy. 
Various versions have evolved over the years, sometimes by accident, 
sometimes on purpose (injected humour and the like).
"""

window = Tk()

horizontal_scrollbar = Scrollbar(window, orient='horizontal')
horizontal_scrollbar.pack(side='bottom', fill=X)

vertical_scrollbar = Scrollbar(window, orient='vertical')
vertical_scrollbar.pack(side='right', fill=Y)

text = Text(
    window, 
    wrap='none', 
    height=10, 
    width=24,
    xscrollcommand=horizontal_scrollbar.set, 
    yscrollcommand=vertical_scrollbar.set
    )
text.insert(INSERT, sample_text)
text.pack()

horizontal_scrollbar.config(command=text.xview)
vertical_scrollbar.config(command=text.yview)

window.mainloop()