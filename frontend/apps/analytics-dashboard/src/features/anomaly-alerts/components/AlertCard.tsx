export default function AlertCard({ alert }: any) {
  return (
    <div className="bg-red-900 p-4 rounded-xl mb-2">
      <div className="font-bold">{alert.title}</div>
      <div>{alert.message}</div>
    </div>
  );
}