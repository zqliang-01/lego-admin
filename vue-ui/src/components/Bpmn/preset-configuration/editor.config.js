export const defaultSettings = {
  processId: `Process_${new Date().getTime()}`,
  processName: `业务流程`,
  processEngine: 'flowable',
  paletteMode: 'enhancement',
  penalMode: 'custom',
  contextPadMode: 'enhancement',
  rendererMode: 'rewrite',
  bg: 'grid-image',
  toolbar: true,
  useMinimap: false,
  useLint: false,
  useMock: true,
  contextmenu: true,
  customContextmenu: true,
  otherModule: true,
  templateChooser: true,
  customTheme: {
    defaultEndEventOpacity: 0.1,
    defaultGatewayOpacity: 0.1,
    defaultIntermediateCatchEventOpacity: 0.1,
    defaultIntermediateThrowEventOpacity: 0.1,
    defaultLabelOpacity: 0.1,
    defaultSequenceOpacity: 0.1,
    defaultStartEventOpacity: 0.1,
    defaultTaskOpacity: 0.1
  }
}
