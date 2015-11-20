%%
%% This is file `eisti-exam.cls',
%% generated with the docstrip utility.
%%
%% The original source files were:
%%
%% eisti-exam.dtx  (with options: `class')
%% 
%% This is a generated file.
%% 
%% Copyright (C) 2012-2013 by Romain DUJOL <romain.dujol@eisti.eu>
%% 
%% This file may be distributed and/or modified under the
%% conditions of the LaTeX Project Public License, either
%% version 1.3 of this license or (at your option) any later
%% version. The latest version of this license is in:
%% 
%%    http://www.latex-project.org/lppl.txt
%% 
%% and version 1.2 or later is part of all distributions of
%% LaTeX version 1999/12/01 or later.
%% 

\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{eisti-exam}
    [2013/01/22 v2.5b Class for exam sheets]


\RequirePackage{ifthen,calc}
\RequirePackage{tabularx,multirow,graphicx}
\RequirePackage{lastpage,fancyhdr}
\RequirePackage{amsthm,enumerate}
\input eisti-common.clo
\newboolean{plainHeader}
\DeclareOption{special}{\setboolean{plainHeader}{false}}
\DeclareOption{plain}  {\setboolean{plainHeader} {true}}
\newboolean{isProject}
\DeclareOption{exam   }{\setboolean{isProject}{false}}
\DeclareOption{project}{\setboolean{isProject} {true}}
\ExecuteOptions{nograde,all,special,exam}
\logo{eisti-logo}
\ProcessOptions\relax
\ifx\eisti@language\@undefined
\else\RequirePackage{babel}\fi
\LoadClassWithOptions{article}
\let\plaintitle =\title
\let\plainauthor=\author
\let\plaindate  =\date
\renewcommand{\title   }[1]{\def\exam@title   {#1}}
\renewcommand{\author  }[1]{\def\exam@author  {#1}}
\renewcommand{\date    }[1]{\def\exam@date    {#1}}
\newcommand{\subject }[1]{\def\exam@subject {#1}}
\newcommand{\duration}[1]{\def\exam@duration{#1}}
\newcommand{\docs    }[1]{\def\exam@docs    {#1}}
\newcommand{\nota    }[1]{\def\exam@nota    {#1}}
\newcommand{\insertsubject }{\exam@subject }
\newcommand{\inserttitle   }{\exam@title   }
\newcommand{\insertauthor}
  {
    \ifthenelse{\isundefined{\exam@author}}{}{\exam@author}
  }
\newcommand{\insertdate}
  {
    \ifthenelse{\isundefined{\exam@date}}{\today}{\exam@date}
  }
\newcommand{\insertduration}{\exam@duration}
\newcommand{\insertdocs}
  {
    \ifthenelse{\isundefined{\exam@docs}}{}{\exam@docs}
  }
\newcommand{\insertnota}
  {
    \ifthenelse{\isundefined{\exam@nota}}{}{\exam@nota}
  }
\ifthenelse{\boolean{plainHeader}}%
  {}
  {
    \let\and\relax
    \let\maketitle\relax

    \newsavebox{\header@rightbox}
    \newcommand{\header@right}
      {
        \begin{lrbox}{\header@rightbox}
        \begin{tabular}{|l|l|}
         \hline \multicolumn{2}{|c|}{\raisebox{0pt}[2em][0em]{\Large\bf \insertgrade \insertcampus}} \\
                \multicolumn{2}{|c|}{\raisebox{0pt}[2em][1em]{\Large\bf \exam@title}}
           \ifthenelse{\isundefined{\exam@author}}%
             {\cr}{\cr\multicolumn{2}{|r|}{\raisebox{0pt}[0em][.75em]{\sl\exam@author}}\cr}
         \hline \textit{\label@subject} : {\bf\exam@subject}
              & \textit{\ifthenelse{\boolean{isProject}}{\label@exam@dueDate}{\label@date}} : {\bf\insertdate}
         \ifthenelse{\boolean{isProject}}%
           {\cr}{\cr\hline {\bf\insertdocs} & \textit{\label@exam@duration}    : {\bf\exam@duration}\cr}
                    \hline {\bf\insertnota} & \textit{\label@pagesnumber} : {\bf 0} \\
         \hline
        \end{tabular}
        \end{lrbox}
      }
    \newsavebox{\header@fullbox}
    \newcounter{header@rows}
    \newcommand{\header@full}[1]
      {
        \setcounter{header@rows}{5}
        \ifthenelse{\isundefined{\exam@author}}{}{\stepcounter{header@rows}}
        \begin{lrbox}{\header@fullbox}
        \begin{tabularx}{\textwidth}{|p{#1}|X|l|}
         \hline \multirow{\theheader@rows}{*}{\includegraphics[width=#1]{\logo@file}}
              & \multicolumn{2}{c|}{\raisebox{0pt}[2em][0em]{\Large\bf \insertgrade \insertcampus}} \\
              & \multicolumn{2}{c|}{\raisebox{0pt}[2em][1em]{\Large\bf \exam@title}}
           \ifthenelse{\isundefined{\exam@author}}%
             {\cr}{\cr&\multicolumn{2}{r|}{\raisebox{0pt}[0em][.75em]{\sl\exam@author}}\cr}
         \cline{2-3} & \textit{\label@subject} : {\bf\exam@subject}
                     & \textit{\ifthenelse{\boolean{isProject}}{\label@exam@dueDate}{\label@date}} : {\bf\insertdate}
         \ifthenelse{\boolean{isProject}}%
           {\cr}{\cr\cline{2-3} & {\bf\insertdocs} & \textit{\label@exam@duration}    : {\bf\exam@duration}\cr}
                    \cline{2-3} & {\bf\insertnota} & \textit{\label@pagesnumber} : {\bf\pageref{LastPage}} \\
         \hline
        \end{tabularx}
        \end{lrbox}
      }
    \newcommand{\exam@maketitle}%
      {
        \header@right
        \newlength{\header@height}\settototalheight{\header@height}{\usebox{\header@rightbox}}
        \newlength{\header@width }\settowidth      {\header@width }{\usebox{\header@rightbox}}
        \newlength{  \logo@width }\settowidth      {  \logo@width }{\includegraphics[height=0.9\header@height]{\logo@file}}
        \ifthenelse{\lengthtest{\header@width < \textwidth}}%
          {\header@full{\minof{\textwidth - \header@width}{\logo@width} * \real{0.9}}}
          {
            \ClassWarning{eisti-exam}{Natural header without logo is too wide so final header will be ugly...}
            \header@full{0.9\logo@width}
          }
        \noindent\usebox{\header@fullbox}\par
      }
  }
\theoremstyle{definition}
\newtheorem*{TextbookQuestions}{\bf \label@exam@TextbookQuestions}
\newtheorem {Exercise} {\bf \label@exam@Exercise}
\newtheorem*{Exercise*}{\bf \label@exam@Exercise}
\newtheorem {Problem}  {\bf \label@exam@Problem}
\newtheorem*{Problem*} {\bf \label@exam@Problem}
\theoremstyle{remark}
\newtheorem*{Remark}{\label@exam@Remark}
\newenvironment   {questions}{\begin{enumerate}[\hspace{.5em}a.]}{\end{enumerate}}
\newenvironment{subquestions}{\begin{enumerate}[\em(i)]}         {\end{enumerate}}
\fancypagestyle{plain}%
  {
    \fancyhf{}
    \cfoot{-~\thepage~/~\pageref{LastPage}~-}
    \renewcommand{\headrulewidth}{0pt}
  }
\AtBeginDocument%
  {
    \pagestyle{plain}
    \ifthenelse{\boolean{plainHeader}}%
      {}{\exam@maketitle\bigskip\par}
  }

\endinput
%%
%% End of file `eisti-exam.cls'.
