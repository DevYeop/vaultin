import React from 'react';
import {
  ScrollView,
  StatusBar,
  StyleSheet,
  Text,
  useColorScheme,
  View,
} from 'react-native';
import {useState, useEffect} from 'react';
import {Colors, Header} from 'react-native/Libraries/NewAppScreen';
import {
  NativeEventEmitter,
  NativeModules,
  DeviceEventEmitter,
} from 'react-native';

function App(): React.JSX.Element {
  const [sharedText, setSharedText] = useState<string | null>('null');

  useEffect(() => {
    console.log('👂 이벤트 리스너 설정 시도 중');
    const sub = DeviceEventEmitter.addListener('onSharedText', text => {
      console.log('📥 JS에서 이벤트 받음:', text);
    });

    return () => sub.remove();
  }, []);

  const isDarkMode = useColorScheme() === 'dark';

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  /*
   * To keep the template simple and small we're adding padding to prevent view
   * from rendering under the System UI.
   * For bigger apps the recommendation is to use `react-native-safe-area-context`:
   * https://github.com/AppAndFlow/react-native-safe-area-context
   *
   * You can read more about it here:
   * https://github.com/react-native-community/discussions-and-proposals/discussions/827
   */
  const safePadding = '5%';

  return (
    <View style={backgroundStyle}>
      <StatusBar
        barStyle={isDarkMode ? 'light-content' : 'dark-content'}
        backgroundColor={backgroundStyle.backgroundColor}
      />

      <ScrollView style={backgroundStyle}>
        <View style={{paddingRight: safePadding}}>
          <Header />
          <Text style={styles.sectionContainer}>sharedText:{sharedText}</Text>
        </View>
      </ScrollView>
    </View>
  );
}

const styles = StyleSheet.create({
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
    color: 'white',
    fontSize: 32,
  },
});

export default App;
