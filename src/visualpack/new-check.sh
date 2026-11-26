
ADMIN@DESKTOP-JRR7VE4 UCRT64 ~/OneDrive/Desktop/myfirstgit (master)
$ ls
exxx.txt  int/

ADMIN@DESKTOP-JRR7VE4 UCRT64 ~/OneDrive/Desktop/myfirstgit (master)
$ touch scope.md

ADMIN@DESKTOP-JRR7VE4 UCRT64 ~/OneDrive/Desktop/myfirstgit (master)
$ echo "THis is a new added information" > scope.md

ADMIN@DESKTOP-JRR7VE4 UCRT64 ~/OneDrive/Desktop/myfirstgit (master)
$ cat scope.md
THis is a new added information

ADMIN@DESKTOP-JRR7VE4 UCRT64 ~/OneDrive/Desktop/myfirstgit (master)
$ mkdir IceCream

ADMIN@DESKTOP-JRR7VE4 UCRT64 ~/OneDrive/Desktop/myfirstgit (master)
$ ls
IceCream/  exxx.txt  int/  scope.md

ADMIN@DESKTOP-JRR7VE4 UCRT64 ~/OneDrive/Desktop/myfirstgit (master)
$ cd IceCream

ADMIN@DESKTOP-JRR7VE4 UCRT64 ~/OneDrive/Desktop/myfirstgit/IceCream (master)
$ touch new_Scope.txt

ADMIN@DESKTOP-JRR7VE4 UCRT64 ~/OneDrive/Desktop/myfirstgit/IceCream (master)
$ echo "A new push in gits or whatever to that" new_Scope.txt
A new push in gits or whatever to that new_Scope.txt

ADMIN@DESKTOP-JRR7VE4 UCRT64 ~/OneDrive/Desktop/myfirstgit/IceCream (master)
$ echo "will this be in below?" new_Scope.txt
will this be in below? new_Scope.txt

ADMIN@DESKTOP-JRR7VE4 UCRT64 ~/OneDrive/Desktop/myfirstgit/IceCream (master)
$ cat new_Scope.txt

ADMIN@DESKTOP-JRR7VE4 UCRT64 ~/OneDrive/Desktop/myfirstgit/IceCream (master)
$ echo "A new recipe for that visual data ice cream!" > new_Scope.txt

ADMIN@DESKTOP-JRR7VE4 UCRT64 ~/OneDrive/Desktop/myfirstgit/IceCream (master)
$ ls
new_Scope.txt

ADMIN@DESKTOP-JRR7VE4 UCRT64 ~/OneDrive/Desktop/myfirstgit/IceCream (master)
$ cd/myfirstgit
bash: cd/myfirstgit: No such file or directory

ADMIN@DESKTOP-JRR7VE4 UCRT64 ~/OneDrive/Desktop/myfirstgit/IceCream (master)
$ cd ..

ADMIN@DESKTOP-JRR7VE4 UCRT64 ~/OneDrive/Desktop/myfirstgit (master)
$ ls
IceCream/  exxx.txt  int/  scope.md

ADMIN@DESKTOP-JRR7VE4 UCRT64 ~/OneDrive/Desktop/myfirstgit (master)
$ git status
On branch master
Untracked files:
  (use "git add <file>..." to include in what will be committed)
        IceCream/
        scope.md

nothing added to commit but untracked files present (use "git add" to track)

ADMIN@DESKTOP-JRR7VE4 UCRT64 ~/OneDrive/Desktop/myfirstgit (master)
$ git log
commit 3caf551a48c6b7efb099c98ee34fdc7e4c71e615 (HEAD -> master)
Author: Gothic_Bat <nrejera.ccsjdm@gmail.com>
Date:   Mon Nov 9 03:30:43 2026 +0800

    sending new

commit 5a555a2403378ca73d6a0069f918153e9e7e72c4
Author: Gothic_Bat <nrejera.ccsjdm@gmail.com>
Date:   Mon Nov 9 03:25:23 2026 +0800

    send

commit cbf78b36a08a245b81673286d133c369339141fd
Author: Gothic_Bat <nrejera.ccsjdm@gmail.com>
Date:   Mon Nov 9 03:11:48 2026 +0800

    New update let's see

commit 095d7ec9f046aa34c908c463d0dda6a0bee1173e
Author: Gothic_Bat <nrejera.ccsjdm@gmail.com>
Date:   Mon Nov 9 03:05:12 2026 +0800

    Commit

ADMIN@DESKTOP-JRR7VE4 UCRT64 ~/OneDrive/Desktop/myfirstgit (master)
$
