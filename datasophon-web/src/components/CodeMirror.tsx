import ReactCodeMirror from '@uiw/react-codemirror'
import { vscodeDark } from '@uiw/codemirror-theme-vscode';
import { javascript } from '@codemirror/lang-javascript';
import {forwardRef} from 'react';

const CodeMirror =forwardRef((props: any, ref: any) => {
  return (
    <ReactCodeMirror
      ref={ref}
      value={props?.value}
      height='700px'
      theme={vscodeDark}
      editable={props?.editable}
      extensions={[javascript({ jsx: true, typescript: true })]}
      basicSetup={{
        tabSize: 2,
        lineNumbers: true,
        foldGutter: true,
        highlightActiveLineGutter: true,
        highlightSpecialChars: true,
        history: true,
        drawSelection: true,
        dropCursor: true,
        allowMultipleSelections: true,
        indentOnInput: true,
        syntaxHighlighting: true,
        bracketMatching: true,
        closeBrackets: true,
        autocompletion: true,
        rectangularSelection: true,
        crosshairCursor: true,
        highlightActiveLine: true,
        highlightSelectionMatches: true,
        closeBracketsKeymap: true,
        defaultKeymap: true,
        searchKeymap: true,
        historyKeymap: true,
        foldKeymap: true,
        completionKeymap: true,
        lintKeymap: true,
      }}
    />
  )
})

export default CodeMirror